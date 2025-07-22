package com.oopsw.foodservice.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.oopsw.foodservice.dto.FoodApiDto;
import com.oopsw.foodservice.dto.FoodDto;
import com.oopsw.foodservice.dto.MemberDto;
import com.oopsw.foodservice.jpa.FoodEntity;
import com.oopsw.foodservice.jpa.FoodTotalEntity;
import com.oopsw.foodservice.repository.FoodApiRepository;
import com.oopsw.foodservice.repository.FoodRepository;
import com.oopsw.foodservice.repository.FoodTotalRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
	private final FoodRepository foodRepository;
	private final FoodApiRepository foodApiRepository;
	private final ModelMapper modelMapper;
	private final FoodTotalRepository foodTotalRepository;
	private final RestTemplate restTemplate;
	private final Environment environment;

	@Override
	public Mono<List<FoodApiDto>> getFoodByNameLike(FoodApiDto foodApiDto) {
		if(foodApiDto.getKeyword() == null || foodApiDto.getKeyword().isBlank()) {
			return Mono.error(new IllegalArgumentException("검색 키워드를 입력해야 합니다."));
		}
		if(foodApiDto.getPageNo() < 1 || foodApiDto.getNumOfRows() < 1) {
			return Mono.error(new IllegalArgumentException("Invalid pageNo or numOfRows"));
		}
		return foodApiRepository.fetchFoodData(foodApiDto.getPageNo(), foodApiDto.getNumOfRows())
			.map(list ->
				// Java 스트림을 이용해 'foodNm'에 keyword 포함된 항목만 필터
				list.stream()
					.filter(item -> {
						// null 검사 추가
						String name = item.getFoodNm();
						return name != null && name.contains(foodApiDto.getKeyword());
					})
					.collect(Collectors.toList())
			);
	}

	@Override
	public FoodDto getIntakeKcal(FoodDto foodDto) {
		List<FoodEntity> foodEntities = foodRepository.findByMemberIdAndIntakeDate(foodDto.getMemberId(), foodDto.getIntakeDate());
		float intakeKcalSum = 0f;
		for (FoodEntity foodEntity : foodEntities) {
			if(foodEntity.getIntakeKcal() != null){
				intakeKcalSum += foodEntity.getIntakeKcal();
			}
		}
		return FoodDto.builder().intakeKcalSum(intakeKcalSum).build();
	}

	@Override
	public List<FoodDto> getYearIntakeKcal(FoodDto foodDto) {
		int year = foodDto.getYear();
		String memberId = foodDto.getMemberId();

		// 가져온 년도로 범위 설정
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year, 12, 31);

		List<FoodEntity> foodEntities = foodRepository.findByMemberIdAndIntakeDateBetween(memberId, java.sql.Date.valueOf(start),java.sql.Date.valueOf(end));

		// 날짜별 IntakeKcal 합계
		Map<Date, Float> IntakeKcalSumMap = new HashMap<>();
		for (FoodEntity foodEntity : foodEntities) {
			Date intakeDate = foodEntity.getIntakeDate();
			float kcal = foodEntity.getIntakeKcal() != null ? foodEntity.getIntakeKcal() : 0f;
			IntakeKcalSumMap.put(intakeDate, IntakeKcalSumMap.getOrDefault(intakeDate, 0f) + kcal);
		}

		List<FoodDto> foodDtoList = new ArrayList<>();
		for (Map.Entry<Date, Float> entry : IntakeKcalSumMap.entrySet()) {
			foodDtoList.add(FoodDto.builder()
				.intakeDate(entry.getKey())
				.intakeKcalSum(entry.getValue())
				.build());
		}
		// 날짜순 정렬
		foodDtoList.sort(Comparator.comparing(FoodDto::getIntakeDate));

		return foodDtoList;
	}

	public List<FoodDto> getYearIntakeAvgAll(FoodDto foodDto) {
		String memberUrl = String.format(environment.getProperty("member-service.like.url"), foodDto.getMemberId());
		ResponseEntity<List<MemberDto>> memberDto = restTemplate.exchange(memberUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<MemberDto>>() {  });
		List<MemberDto> memberIds = memberDto.getBody();

		Map<LocalDate, List<Float>> dateToKcalList = new TreeMap<>();

		for(MemberDto member : memberIds) {
			FoodDto perMemberDto = FoodDto.builder()
				.year(foodDto.getYear())
				.memberId(member.getMemberId())
				.build();

			List<FoodDto> oneMemberData = getYearIntakeKcal(perMemberDto);

			for(FoodDto daily : oneMemberData) {
				LocalDate localDate = daily.getIntakeDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				dateToKcalList.computeIfAbsent(localDate, k -> new ArrayList<>())
					.add(daily.getIntakeKcalSum());
			}
		}

		List<FoodDto> resultDtos = dateToKcalList.entrySet().stream()
			.map(entry -> {
				LocalDate date = entry.getKey();
				List<Float> kcals = entry.getValue();
				float average = (float) kcals.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);
				return FoodDto.builder()
					.intakeDateLocal(date)
					.intakeAvg(average)
					.build();
			}).collect(Collectors.toList());

		return resultDtos;
	}

	@Override
	public List<FoodDto> getFood(FoodDto foodDto) {
		List<FoodEntity> foodEntities = foodRepository.findByMemberIdAndIntakeDate(foodDto.getMemberId(), foodDto.getIntakeDate());
		List<FoodDto> foodDtoList = new ArrayList<>();
		for (FoodEntity foodEntity : foodEntities) {
			foodDtoList.add(modelMapper.map(foodEntity, FoodDto.class));
		}
		return foodDtoList;
	}

	@Override
	public FoodDto addFood(FoodDto foodDto) {
		FoodEntity foodEntity = modelMapper.map(foodDto, FoodEntity.class);
		// foodEntity.setFoodId(String.format("f%04d", (foodRepository.findAll().size())+1));
		foodEntity.setFoodId(UUID.randomUUID().toString());
		foodEntity.setIntakeKcal(foodDto.getIntake()/100f*foodDto.getUnitKcal());
		foodRepository.save(foodEntity);

		LocalDate localDate = foodEntity.getIntakeDate().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate();

		if(foodTotalRepository.existsByMemberIdAndIntakeDate(foodEntity.getMemberId(), localDate)) {
			FoodTotalEntity foodTotalEntity = foodTotalRepository.findByMemberIdAndIntakeDate(foodEntity.getMemberId(), localDate);
			foodTotalEntity.setFoodTotalKcal(foodTotalEntity.getFoodTotalKcal() + foodEntity.getIntakeKcal());

			foodTotalEntity.setIntakeDate(localDate);
			foodTotalRepository.save(foodTotalEntity);
		}
		else {
			FoodTotalEntity foodTotalEntity = FoodTotalEntity.builder()
				.foodTotalId(UUID.randomUUID().toString())
				.foodTotalKcal(foodDto.getIntake()/100f*foodDto.getUnitKcal())
				.memberId(foodEntity.getMemberId())
				.intakeDate(localDate)
				.build();
			foodTotalRepository.save(foodTotalEntity);
		}


		return foodDto;
	}

	@Override
	@Transactional
	public FoodDto setFood(FoodDto foodDto) {
		FoodEntity foodEntity = foodRepository.findByMemberIdAndFoodId(foodDto.getMemberId(), foodDto.getFoodId());
		float beforeIntakekcal = foodEntity.getIntakeKcal();
		if(foodEntity == null) {
			throw new IllegalArgumentException("Invalid foodId");
		}

		float intakeKcal = foodDto.getIntake()/100f*foodEntity.getUnitKcal();
		foodEntity.setIntake(foodDto.getIntake());
		foodEntity.setIntakeKcal(intakeKcal);
		foodRepository.save(foodEntity);

		LocalDate localDate = foodEntity.getIntakeDate().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate();

		FoodTotalEntity foodTotalEntity = FoodTotalEntity.builder()
			.memberId(foodDto.getMemberId())
			.intakeDate(localDate)
			.build();

		FoodTotalEntity foodTotalEntityafter = foodTotalRepository.findByMemberIdAndIntakeDate(foodTotalEntity.getMemberId(), foodTotalEntity.getIntakeDate());
		foodTotalEntityafter.setFoodTotalKcal(foodTotalEntityafter.getFoodTotalKcal() + (intakeKcal - beforeIntakekcal));
		foodTotalRepository.save(foodTotalEntityafter);
		return foodDto;
	}

	@Override
	@Transactional
	public void removeFood(FoodDto foodDto) {
		FoodEntity foodEntityDateIntakeKcal = foodRepository.findByMemberIdAndFoodId(foodDto.getMemberId(), foodDto.getFoodId());
		LocalDate localDate = foodEntityDateIntakeKcal.getIntakeDate().toInstant()
			.atZone(ZoneId.systemDefault())
			.toLocalDate();
		FoodTotalEntity foodTotalEntity = foodTotalRepository.findByMemberIdAndIntakeDate(foodDto.getMemberId(), localDate);

		int foodEntity = foodRepository.deleteByMemberIdAndFoodId(foodDto.getMemberId(), foodDto.getFoodId());
		foodTotalEntity.setFoodTotalKcal(foodTotalEntity.getFoodTotalKcal()-foodEntityDateIntakeKcal.getIntakeKcal());
		foodTotalRepository.save(foodTotalEntity);

		if(foodEntity == 0) {
			throw new IllegalArgumentException("Invalid memberId or foodId");
		}

	}
}
