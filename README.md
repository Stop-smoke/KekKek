![1  KekKek_인트로(아키텍처 포함)](https://github.com/Stop-smoke/KekKek/assets/141006937/da5dc36e-1439-4019-8079-e2744e5ae598)
![2  KekKek_프로젝트 기술 스택(3)](https://github.com/Stop-smoke/KekKek/assets/141006937/bfdd8e87-68c3-4408-b6c3-d651d90c5210)

<br><br>

# 🙂 STOPSMOKE TEAM을 소개합니다!
<br><br><br>
<div align="center">
  <table>
    <tbody>
      <tr>
        <td align="center"><a href="https://github.com/agvber"><img src="https://github.com/Stop-smoke/KekKek/assets/141006937/76f7a481-28e0-4d02-b142-46b56de33009" width="100px;"><br /><sub><b>김민준</b></sub></a><br /></a></td>
        <td align="center"><a href="https://github.com/xeejin"><img src="https://github.com/Stop-smoke/KekKek/assets/141006937/0b7eb0cc-c1bf-489f-a2ad-f59f69850809" width="100px;"><br /><sub><b>임희진</b></sub></a><br /></a></td>
        <td align="center"><a href="https://github.com/dongwonyang"><img src="https://github.com/Stop-smoke/KekKek/assets/141006937/302fdacf-42cb-478d-a956-c365d316c393" width="100px;"><br /><sub><b>양동원</b></sub></a><br /></a></td>
        <td align="center"><a href=""><img src="https://github.com/Stop-smoke/KekKek/assets/141006937/bbaab4a7-24c4-4627-bd55-f185c5c6fdc7" width="100px;"><br /><sub><b>박세영</b></sub></a><br /></a></td>
      </tr>
      <tr>
        <td align="center"><p>- Python<br>- JavaScript<br>- HTML/CSS</p></td>
        <td align="center"><p>- Java<br>- Spring<br>- MySQL</p></td>
        <td align="center"><p>- C++<br>- Node.js<br>- MongoDB</p></td>
        <td align="center"><p>- Ruby<br>- Rails<br>- PostgreSQL</p></td>
      </tr>
    </tbody>
  </table>
</div>
<br>
<br><br><br><br>




# 🛠️ 기술적 의사결정

  <br>
  <br><br><br><br><br><br><br><br><br>



  
# 💥 트러블 슈팅

  <br>
  
### ▶ Result에 따라 핸들링 처리가 복잡해진 문제

<br>

```kotlin
override suspend fun editPost(postEntity: PostEntity): Result<Unit> {
        return try {
            val updateMap = mapOf(
                "category" to postEntity.category,
                "title" to postEntity.title,
                "text" to postEntity.text,
                "date_time" to postEntity.dateTime
            )
            firestore.collection(COLLECTION)
                .document(postEntity.id ?: return Result.Error(NullPointerException()))
                .update(updateMap)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
```
<br>

- **문제 상황** 
    - 기존에는 모든 영역에서 `Result`를 감싼 형태로 값을 리턴하는 설계를 사용하고 있었습니다. 이 방식은 데이터 결과 여부를 판단하는 데는 편리했지만, 다른 영역에서 `Result`에 따라 핸들링 처리가 복잡해지는 문제가 있었습니다.<br><br>

- **해결 방법** 
    - 이 문제를 해결하기 위해, 프레젠테이션에서 필요한 에러를 throw 처리하여 ViewModel에서 catch 하도록 변경하였습니다. 그렇게 catch한 에러를 `UiState`를 사용하여 사용자에게 보여주도록 하였습니다. 이 변경으로 인해 코드 유지 보수와 복잡도가 줄어들었습니다. <br><br>

### ▶ Firebase 회원 탈퇴 정책 문제 <br><br>

- **문제상황 (FirebaseAuth Recent Login Required Exception)** <br>
    - firebase는 계정 삭제, 기본 이메일 주소 설정, 비밀번호 변경과 같이 보안에 민감한 작업을 하려면 사용자가 최근에 로그인한 적이 있어야 합니다. 그로인해 가입된 유저가 회원 탈퇴를 요청하는 경우 재인증해야 하는 문제가 있었습니다. 시중에 있는 앱 중에서 카카오 로그인, 구글 로그인 후에 회원 탈퇴 했을 경우에 재인증을 요청하는 앱을 보지 못하였기 때문에, 이 문제가 유저 경험을 떨어뜨릴 가능성이 크다고 판단하였습니다.<br><br>

- **해결 방법**<br>
    - 프론트에서는 신뢰할 수 없는 경우가 존재 할 경우에 이러한 정책을 내놓은 것으로 판단하였습니다. 따라서, `Cloud Function`을 사용하여 서버에 삭제 요청을 하는 방향성으로 변경하였습니다.<br><br>

### ▶ PagingData 아이템 추가 문제<br><br>

<img src="https://github.com/Stop-smoke/KekKek/assets/141006937/7c858fee-bc18-46be-a211-7d939b2778fc" width="300" height="" title="" alt=""></img>

- **문제상황**<br>
    - PagingData는 아이템을 추가하기 위해서 2가지 방법이 존재하였습니다. 첫번째는 `PagingAdapter` 를 `Refresh` 하는 방식, 두번째는 `RemoteMediator`를 사용하여 네트워크 데이터를 데이터 베이스에 저장하는 방식입니다. 전자는 API 요청이 비용이 너무 크다는 것에 대한 문제점이 존재하였고, 후자는 오프라인 모드를 지원할 생각이 없었기 때문에 이것 또한 비용이 큰 문제가 있다고 생각 했습니다.<br><br>

- **해결 방법**<br>
    - 이 문제를 해결하기 위해, `ConcatAdapter`를 사용해서 기존 아이템과 새로운 아이템의 RecyclerView Adapter를 나누었습니다. 새로운 아이템을 업데이트 할 경우에, 전체 아이템을 Refresh 하지 않고 새로운 아이템 부분만 추가하는 방식으로 진행하였습니다.<br><br>

### ▶ EXIF 메타데이터 처리 문제<br><br>

![Untitled](https://github.com/Stop-smoke/KekKek/assets/141006937/ebfce04f-676e-4618-b88f-3339a47439db)

- **문제 상황**<br>
    - 마이 페이지의 계정이나, 글쓰기의 에디터 기능에서 사진을 첨부하는 과정에서 어떤 이미지는 90도가 돌아가는 현상이 발생하였습니다. 이는 이미지 파일에 내장된 EXIF 방향 메타데이터가 원인이었습니다. ActivityResultContracts.PickVisualMedia를 사용할 때 EXIF 메타데이터가 이미지 파일에 포함되는데 이를 적절히 처리하지 않으면 이미지가 회전되어 보일 수 있습니다.<br><br>

- **해결 방법**<br>
    - 이 문제를 해결하기 위해, uri로부터 inputstream을 구하고, inputstream으로부터 ExifInterface 를 구하고, getAttributeInt 를 이용해 이미지의 방향 값을 구하였습니다. 방향값을 matrix 의 속성으로 넣고 해당 matrix 로 방향이 수정된 비트맵 이미지를 만들었습니다.<br><br>

### ▶ 금연 테스트 화면 구현 중 뷰페이저 어댑터가 제대로 작동하지 않는 문제<br><br>

- **문제상황**<br>
    - Question Fragment에서 ViewPagerFragment의 메서드를 호출할 때 binding 이 null 이어서 NullPointerException이 발생하였습니다. (parentFragment as? ViewPagerFragment)?.moveToNextQuestionPage() 를 사용해 접근해보려고 했지만, 로그를 찍어본 결과 ViewPagerFragment는 부모 프래그먼트가 아니였습니다.<br><br>

- **해결 방법**<br>
    - 프래그먼트간 계층 관계를 피하기 위해 shared view model 을 사용해서 Question Fragment에서 live data를 세팅하고, ViewPagerFragment에서 옵저빙 하는 방식으로 해결하였습니다. 이를 통해 프래그먼트간 정보를 주고받을 땐 `Shared view model` 을 고려해보는 것이 좋겠다고 느꼈습니다.<br><br>

### ▶ ViewModel 데이터 처리 중 Fragment 종료<br><br>

<img src="https://github.com/Stop-smoke/KekKek/assets/141006937/67b84692-2adf-442d-a05a-ad4c281e9f28" width="500" height="" title="" alt=""></img>

- **문제 상황**<br>
    - DialogFragment에서 비동기 데이터 처리가 진행되는 동안 `dismiss()`가 호출되어 viewModel이 죽는 문제가 있었습니다. 이로 인해 데이터 처리가 정상적으로 이루어지지 않았습니다.<br><br>

- **해결 방법**<br>
    - 이 문제를 해결하기 위해, parameter로 전달해줘서 viewModel 내부 데이터 처리 완료 후 `dismiss()`를 호출하도록 하였습니다.<br><br>

### ▶ Firestore 조회수 카운트 하는 문제<br><br>

![그림02](https://github.com/Stop-smoke/KekKek/assets/141006937/e56b4325-8678-4e8b-a1c2-5aa5c6724d4b)

- **문제 상황**<br>
    - `Cloud Firestore`는 초당 최대 1회 문서 업데이트를 보증하므로, 조회수를 카운트하는 것이 불가능한 문제가 있었습니다. 이는 Firestore의 한계로 인한 문제였습니다.<br><br>

- **해결 방법**<br>
    - 이 문제를 해결하기 위해, 분산 카운터 솔루션을 도입하였습니다. 이 방식은 샤드로 구성된 하위 컬렉션을 사용하여 각 샤드의 값을 합산하는 방식을 적용하였습니다. 이를 통해 단일 카운터보다 10배 많은 쓰기를 효율적으로 처리할 수 있게 되었습니다. 앞으로 Firestore에서 조회수를 카운트할 때는 이 분산 카운터 솔루션을 도입하여 사용할 계획입니다.<br><br>

### ▶ Hilt Single Instance 주입 문제<br><br>

- **문제 상황**<br>
    - Hilt 의존성 주입을 사용하면 생명주기를 선언할 수 있습니다. 그러나, SingletonComponent::class를 선언하여 DataStore를 의존성 주입했을 때, 예상치 못한 에러가 발생하였습니다.<br><br>

- **해결 방법**<br>
    - `Hilt()`의 SingletonComponent::class는 애플리케이션 생성 생명주기에서 인스턴스를 생성하지만, 항상 동일한 인스턴스를 제공하지는 않는다는 사실을 알게 되었습니다. 이를 해결하기 위해 @Singleton 애너테이션을 추가하여 하나의 인스턴스만 생성되도록 수정하였습니다. 이로써 Hilt를 사용할 때 `@Singleton` 애너테이션을 통해 단일 인스턴스를 보장할 수 있다는 점을 깨달았습니다.<br><br><br><br><br><br><br><br><br>

  
# 🌍 프로젝트 MVP 기능별 미리보기
  <br>
<details>
<summary>프로젝트 기능별 미리보기 (주의 : 이미지 길이가 매우 깁니다.)</summary>
<div markdown="1">

![KekKek_화면설명_로그인](https://github.com/Stop-smoke/KekKek/assets/141006937/c591e6ae-e21e-4636-ba52-7b64cadd08f3)
![KekKek_화면설명_홈](https://github.com/Stop-smoke/KekKek/assets/141006937/e59c9290-59fb-4dd0-89c7-a32289a4cb03)
![KekKek_화면설명_커뮤니티](https://github.com/Stop-smoke/KekKek/assets/141006937/d1700494-bfaa-4f6a-bebe-478de3b5926a)
![KekKek_화면설명_마이페이지](https://github.com/Stop-smoke/KekKek/assets/141006937/6c1e3658-07ca-452b-8fcf-7d7e73b62e26)
![KekKek_화면설명_설정](https://github.com/Stop-smoke/KekKek/assets/141006937/da2d8e58-7f83-4650-8415-b2cdc0132834)


</div>
</details>
  <br>
  <br><br><br><br><br><br><br><br><br>

  
# ☁ 앞으로의 계획
![앞으로의 계획](https://github.com/Stop-smoke/KekKek/assets/141006937/37d510a2-3e40-428e-933c-9e816b8dc507)

- 다양한 언어에 대응 (한국어/영어)
- 다크모드 대응
- 게스트모드 추가 
