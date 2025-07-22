package com.oopsw.exerciseservice.service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.oopsw.exerciseservice.dto.ExerciseDto;
import com.oopsw.exerciseservice.dto.MemberDto;
import com.oopsw.exerciseservice.jpa.ExerciseEntity;
import com.oopsw.exerciseservice.jpa.ExerciseTotalEntity;
import com.oopsw.exerciseservice.repository.ExerciseRepository;
import com.oopsw.exerciseservice.repository.ExerciseApiRepository;
import com.oopsw.exerciseservice.repository.ExerciseTotalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

	private final ExerciseRepository exerciseRepository;
	private final ExerciseTotalRepository exerciseTotalRepository;
	private final ExerciseApiRepository exerciseApiRepository;
	private final Environment environment;
	private final RestTemplate restTemplate;

	@Override
	public String addExercise(ExerciseDto exerciseDto) {

		String memberUrl = String.format(environment.getProperty("member-service.url"), exerciseDto.getMemberId());
		ResponseEntity<MemberDto> memberDto = restTemplate.exchange(memberUrl, HttpMethod.GET, null, MemberDto.class);
		MemberDto memberInfo = memberDto.getBody();

		float kcal = memberInfo.getWeight()
			* exerciseDto.getMet()
			* exerciseDto.getExerciseMin() / 60f;

		// DTO → Entity 매핑
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.exerciseMin(exerciseDto.getExerciseMin())
			.exerciseName(exerciseDto.getExerciseName())
			.met(exerciseDto.getMet())
			.memberId(exerciseDto.getMemberId())
			.exerciseDate(exerciseDto.getExerciseDate())
			.exerciseId(UUID.randomUUID().toString())
			.exerciseKcal(kcal)
			.build();

		// JPA 저장
		exerciseRepository.save(exerciseEntity);

		if(exerciseTotalRepository.existsByMemberIdAndExerciseDate(exerciseEntity.getMemberId(), exerciseEntity.getExerciseDate())) {
			ExerciseTotalEntity exerciseTotalEntity = exerciseTotalRepository.findByMemberIdAndExerciseDate(exerciseEntity.getMemberId(), exerciseEntity.getExerciseDate());
			exerciseTotalEntity.setExerciseTotalKcal(exerciseTotalEntity.getExerciseTotalKcal() + kcal);
			exerciseTotalRepository.save(exerciseTotalEntity);
		}
		else {
			ExerciseTotalEntity exerciseTotalEntity = ExerciseTotalEntity.builder()
				.exerciseTotalId(UUID.randomUUID().toString())
				.exerciseTotalKcal(kcal)
				.exerciseDate(exerciseDto.getExerciseDate())
				.memberId(exerciseEntity.getMemberId())
				.build();
			exerciseTotalRepository.save(exerciseTotalEntity);
		}
		return exerciseDto.getExerciseId();
	}

	@Override
	public List<ExerciseDto> getExercises(ExerciseDto exerciseDto) {
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.exerciseDate(exerciseDto.getExerciseDate())
			.memberId(exerciseDto.getMemberId())
			.build();
		List<ExerciseEntity> exerciseEntities = exerciseRepository.findByExerciseDateAndMemberId(exerciseEntity.getExerciseDate(), exerciseEntity.getMemberId());
		List<ExerciseDto> exerciseDtos = new ArrayList<>();
		for (ExerciseEntity exerciseEntity1 : exerciseEntities) {
			ExerciseDto dto = ExerciseDto.builder()
				.exerciseId(exerciseEntity1.getExerciseId())
				.exerciseName(exerciseEntity1.getExerciseName())
				.exerciseDate(exerciseEntity1.getExerciseDate())
				.exerciseMin(exerciseEntity1.getExerciseMin())
				.exerciseKcal(exerciseEntity1.getExerciseKcal())
				.build();
			exerciseDtos.add(dto);
		}
		return exerciseDtos;
	}

	@Override
	@Transactional
	public void removeExercise(ExerciseDto exerciseDto) {
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.memberId(exerciseDto.getMemberId())
			.exerciseId(exerciseDto.getExerciseId())
			.build();
		ExerciseEntity exercise = exerciseRepository.findByMemberIdAndExerciseId(exerciseDto.getMemberId(), exerciseDto.getExerciseId());
		ExerciseTotalEntity exerciseTotalEntity = exerciseTotalRepository.findByMemberIdAndExerciseDate(exerciseDto.getMemberId(), exercise.getExerciseDate());

		if (exerciseRepository.existsByMemberIdAndExerciseId(exerciseEntity.getMemberId(), exerciseEntity.getExerciseId())) {
			exerciseRepository.deleteByMemberIdAndExerciseId(exerciseDto.getMemberId(), exerciseEntity.getExerciseId());
			exerciseTotalEntity.setExerciseTotalKcal(exerciseTotalEntity.getExerciseTotalKcal() - exercise.getExerciseKcal());
			exerciseTotalRepository.save(exerciseTotalEntity);
		}
	}

	@Override
	public void setExerciseMin(ExerciseDto exerciseDto) {
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.memberId(exerciseDto.getMemberId())
			.exerciseId(exerciseDto.getExerciseId())
			.build();
		ExerciseEntity exercise = exerciseRepository.findByExerciseId(exerciseEntity.getExerciseId());

		float beforeKcal = exercise.getExerciseKcal();
		exercise.setExerciseMin(exerciseDto.getNewMin());

		String memberUrl = String.format(environment.getProperty("member-service.url"), exerciseDto.getMemberId());
		ResponseEntity<MemberDto> memberDto = restTemplate.exchange(memberUrl, HttpMethod.GET, null, MemberDto.class);
		MemberDto memberInfo = memberDto.getBody();

		float weight = memberInfo.getWeight();
		float met = exercise.getMet();
		float kcal = weight * met * exerciseDto.getNewMin() / 60f;

		exercise.setExerciseKcal(kcal);
		exerciseRepository.save(exercise);

		ExerciseTotalEntity exerciseTotalEntity = ExerciseTotalEntity.builder()
			.memberId(exerciseDto.getMemberId())
			.exerciseDate(exercise.getExerciseDate())
			.build();

		ExerciseTotalEntity exerciseTotalEntityafter = exerciseTotalRepository.findByMemberIdAndExerciseDate(exerciseTotalEntity.getMemberId(), exerciseTotalEntity.getExerciseDate());
		exerciseTotalEntityafter.setExerciseTotalKcal(exerciseTotalEntityafter.getExerciseTotalKcal()+(kcal-beforeKcal));
		exerciseTotalRepository.save(exerciseTotalEntityafter);
	}

	@Override
	public ExerciseDto getExerciseKcal(ExerciseDto exerciseDto) {
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.exerciseDate(exerciseDto.getExerciseDate())
			.memberId(exerciseDto.getMemberId())
			.build();
		List<ExerciseEntity> exerciseEntities = exerciseRepository.findByExerciseDateAndMemberId(exerciseEntity.getExerciseDate(), exerciseEntity.getMemberId());
		float exerciseSum=0;
		for(ExerciseEntity exerciseEntity1 : exerciseEntities) {
			exerciseSum += exerciseEntity1.getExerciseKcal();
		}
		ExerciseDto resultDto = ExerciseDto.builder()
			.exerciseSum(exerciseSum)
			.build();
		return resultDto;
	}

	@Override
	public List<ExerciseDto> getYearExerciseAvgAll(ExerciseDto exerciseDto) {
		String memberUrl = String.format(environment.getProperty("member-service.like.url"), exerciseDto.getMemberId());
		ResponseEntity<List<MemberDto>> memberDto = restTemplate.exchange(memberUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<MemberDto>>() {  });
		List<MemberDto> memberIds = memberDto.getBody();

		Map<LocalDate, List<Float>> dateToKcalList = new TreeMap<>();

		for(MemberDto member : memberIds) {
			ExerciseDto perMemberDto = ExerciseDto.builder()
				.year(exerciseDto.getYear())
				.memberId(member.getMemberId())
				.build();

			List<ExerciseDto> oneMemberData = getYearExerciseKcal(perMemberDto);

			for(ExerciseDto daily : oneMemberData) {
				dateToKcalList.computeIfAbsent(daily.getExerciseDate(), k -> new ArrayList<>())
					.add(daily.getExerciseSum());
			}
		}

		List<ExerciseDto> resultDtos = dateToKcalList.entrySet().stream()
			.map(entry -> {
				LocalDate date = entry.getKey();
				List<Float> kcals = entry.getValue();
				float average = (float) kcals.stream().mapToDouble(Float::doubleValue).average().orElse(0.0);
				return ExerciseDto.builder()
					.exerciseDate(date)
					.exerciseAvg(average)
					.build();
			}).collect(Collectors.toList());

		return resultDtos;
	}


	@Override
	public List<ExerciseDto> getYearExerciseKcal(ExerciseDto exerciseDto) {
		ExerciseEntity exerciseEntity = ExerciseEntity.builder()
			.memberId(exerciseDto.getMemberId())
			.build();

		LocalDate start = LocalDate.parse(exerciseDto.getYear() + "-01-01");
		LocalDate end = LocalDate.parse(exerciseDto.getYear() + "-12-31");


		List<ExerciseEntity> exerciseEntities = exerciseRepository.findByMemberIdAndExerciseDateBetween(exerciseEntity.getMemberId(), start, end);

		// 날짜별 그룹핑 + kcal 합산
		Map<LocalDate, Float> kcalPerDate = exerciseEntities.stream()
			.collect(Collectors.groupingBy(
				ExerciseEntity::getExerciseDate,
				Collectors.summingDouble(ExerciseEntity::getExerciseKcal)
			)).entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> e.getValue().floatValue()
			));

		//날짜별 sorting
		Map<LocalDate, Float> sortedKcalPerDate = new TreeMap<>(kcalPerDate);

		// Map -> ExerciseDto
		List<ExerciseDto> exerciseDtos = sortedKcalPerDate.entrySet().stream()
			.map(entry -> ExerciseDto.builder()
				.exerciseDate (entry.getKey())
				.exerciseSum(entry.getValue())
				.build())
			.collect(Collectors.toList());

		return exerciseDtos;
	}

	public Mono<List<ExerciseDto>> getExerciseOpenSearch(ExerciseDto exerciseDto) {
		if (exerciseDto.getKeyword() == null || exerciseDto.getKeyword().isBlank()) {
			return Mono.error(new IllegalArgumentException("검색 키워드를 입력해야 합니다."));
		}
		if (exerciseDto.getPageNo() < 1 || exerciseDto.getNumOfRows() < 1) {
			return Mono.error(new IllegalArgumentException("pageNo와 numOfRows는 1 이상이어야 합니다."));
		}

		return exerciseApiRepository.fetchExerciseData(exerciseDto.getPageNo(), exerciseDto.getNumOfRows())
			.map(list ->
				// null-safe 필터링
				(list != null ? list.stream()
					.filter(item -> {
						String name = item.getExerciseName();
						return name != null && name.contains(exerciseDto.getKeyword());
					})
					.collect(Collectors.toList())
					: Collections.emptyList())
			);
	}

}
