<br/>

## 📌 프로젝트 개요

- 프로젝트명: REST API 서비스 리팩터링  
- 개요: 클라이언트와 서버를 분리하여 구조를 개선하기 위한 리팩터링

<br/>

## 🏗️ 소프트웨어 아키텍처
![3차 스프린트](./3차%20스프린트.png)

<br/>

## 🛠️ 주요 변경 사항

 - Front - Back 서버 분리
 - Thymeleaf와 @Controller 삭제
 - 서버 분리로 인한 JWT 활용

<br/>

## 💻 추가 개발 환경

### 🧠 BACKEND
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)

### ☁️ 배포 및 인프라
![EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=flat-square&logo=amazon-ec2&logoColor=white)

## ✨ 회고

> 서버를 나누게 되면 **독립적인 개발과 배포**가 가능하다는 점에서 인상 깊었다.  
> 이전에는 UI나 서버 중 하나만 수정해도 전체 서버를 재가동하는 것이 당연하다고 생각했지만,  
> 서버를 분리하면서 **결합도가 낮아지고 효율적인 운영**이 가능해지는 것을 체감했다.  
> 앞으로도 효율적인 개발 및 운영을 위해 이런 구조적 분리를 적극적으로 고려해야겠다고 느꼈다.
