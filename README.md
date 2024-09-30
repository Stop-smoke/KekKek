![1  KekKek_인트로(아키텍처 포함)](https://github.com/Stop-smoke/KekKek/assets/141006937/da5dc36e-1439-4019-8079-e2744e5ae598)
![2  KekKek_프로젝트 기술 스택(3)](https://github.com/Stop-smoke/KekKek/assets/141006937/bfdd8e87-68c3-4408-b6c3-d651d90c5210)

<br><br>

# 🙂 STOPSMOKE TEAM을 소개합니다!
<br><br><br>
<div align="center">
  <table>
    <tbody>
      <tr>
        <td align="center"><a href="https://github.com/agvber"><img src="https://avatars.githubusercontent.com/u/60022205?v=4" width="100px;"><br /><sub><b>김민준</b></sub></a><br /></a></td>
        <td align="center"><a href="https://github.com/xeejin"><img src="https://avatars.githubusercontent.com/u/141006937?v=4" width="100px;"><br /><sub><b>임희진</b></sub></a><br /></a></td>
        <td align="center"><a href="https://github.com/dongwonyang"><img src="https://avatars.githubusercontent.com/u/121853823?v=4" width="100px;"><br /><sub><b>양동원</b></sub></a><br /></a></td>
        <td align="center"><a href="https://github.com/se05503"><img src="https://avatars.githubusercontent.com/u/115936076?v=4" width="100px;"><br /><sub><b>박세영</b></sub></a><br /></a></td>
      </tr>
      <tr>
        <td align="center"><p>Leader<br>Core-Dev<br>Test(Unit)<br>기술적 의사결정</p></td>
        <td align="center"><p>Sub-Leader<br>TF/일정관리<br>개발<br>UI/UX/Design</p>기술적 의사결정(중재)</td>
        <td align="center"><p>member<br>Core-Dev<br>Test(Unit)<br>기술적 의사결정</p></td>
        <td align="center"><p>member<br>QA<br>Test(Product)<br>개발<br>배포</p></td>
      </tr>
    </tbody>
  </table>
</div>
<br>
<br><br><br><br>




# 🛠️ 기술적 의사결정

  <br>

### ▶ 클린아키텍처를 선택한 이유

저희는 애플리케이션 초기 개발 단계에서 여러 라이브러리 선택과 지속적으로 변화하는 데이터베이스, 서버, UI를 대응해야 하는 문제에 직면했습니다. 이러한 문제를 해결하기 위해, 저희는 도메인 중심 설계 아키텍처를 찾게 되었습니다.

도메인 중심 설계 아키텍처에는 **`레이어드 아키텍처`** , **`클린 아키텍처`, `헥사고날 아키텍처`** 등이 대표적으로 있습니다. 이 중에서 저희는 클린 아키텍처를 선택하게 되었습니다. 그 이유는 다음과 같습니다:

- 레이어드 아키텍처는 도메인 레이어가 비대해지면서 영역 간에 오염될 수 있는 단점이 있어 제외하였습니다.
- 헥사고날 아키텍처는 포트와 어댑터를 정확한 구현 형태로 제공하지만, 소규모 도메인 프로젝트 진행 시나 안드로이드에서는 적합하지 않다고 판단하여 제외하였습니다.

클린 아키텍처는 애플리케이션의 비즈니스 로직과 데이터 로직 부분을 분리함으로써, 각각의 컴포넌트가 독립적으로 동작할 수 있도록 설계하였습니다. 이로 인해 독립적인 컴포넌트의 재사용성이 높아지고, 테스트가 용이해지며, 다른 시스템 요소들의 변경에 대해 더 **`OCP 원칙(Open-Closed Principle)`** 을 준수하는 장점이 있습니다.

또한, 특정 라이브러리가 더 이상 지원되지 않거나 더 나은 대안이 등장했을 때, 클린 아키텍처는 기존 코드의 의존성 방향성이 도메인 레이어를 향하므로, 대대적인 코드 리팩토링 없이 라이브러리를 쉽게 변경할 수 있는 장점이 있습니다.

결론적으로, 클린 아키텍처는 세부사항을 즉시 고려하지 않아도 되는 점과 제한적인 시간에서 변경이 최소화된 의존성 그래프 덕분에 현재 저희 팀에서 적합한 아키텍처라고 생각하였기 때문에 선택하였습니다.<br><br><br>

### ▶ 검색 기능에 Algolia를 선택한 이유

저희 프로젝트에서는 Firestore에 저장된 데이터를 기반으로 검색 인덱싱 서버를 구축해야 했습니다. 이 과정에서 Elasticsearch와 Algolia 두 가지 후보를 고려하였습니다.

저희가 Algolia를 선택한 이유는 다음과 같습니다:

1. **Firestore와의 쉬운 통합**:
Algolia는 Firestore와의 연결이 상대적으로 간편하여, 검색 인덱스 구축과 유지 관리가 더 수월합니다. 반면, Elasticsearch는 Firestore와의 통합이 복잡할 수 있어 추가적인 작업이 필요합니다.
2. **Pagination 지원**:
Algolia는 기본적으로 Pagination을 지원하며, 이는 Paging3.0 라이브러리와 잘 통합됩니다. 이로 인해 UI 구현이 더 간단하고 직관적입니다. 검색 결과를 페이지 단위로 나누어 표시하는 것이 쉬워지며, 사용자 경험이 향상된다고 생각했습니다.
3. **빠른 검색 속도**:
Algolia는 높은 검색 속도와 실시간 검색 기능을 제공하여, 사용자에게 빠르고 정확한 검색 결과를 제공합니다. 이는 사용자 경험을 향상시키는 중요한 요소라고 생각했습니다.
4. **관리와 운영의 용이성**:
Algolia는 관리 대시보드와 API가 잘 설계되어 있어, 검색 인덱스의 관리와 모니터링이 용이합니다. Elasticsearch에 비해 설정과 운영이 비교적 간단합니다.

이러한 이유로 저희는 Algolia를 선택하였습니다. Algolia의 강력한 검색 기능과 사용의 용이성 덕분에 프로젝트의 검색 기능을 효율적으로 구현할 수 있었습니다. <br><br><br>

### ▶ SharedViewModel을 사용한 이유

안드로이드 애플리케이션에서 하나의 `Activity`와 여러 개의 `Fragment`가 있을 때, 데이터 관리를 `Activity`를 기준으로 처리하는 경우가 많습니다. 이때 `ViewModel`을 공유하여 데이터 전달을 효율적으로 처리할 수 있습니다. 데이터 전달 방식에는 여러 가지가 있으며, 각각의 장단점이 있습니다.

### 데이터 전달 방식

1. **Bundle과 FragmentManager를 사용한 전달**
2. **Fragment Result API를 사용한 데이터 전달**
3. **공통의 ViewModel (예: HostActivity의 ViewModel)을 사용한 데이터 전달**
4. **Jetpack Navigation의 Safe-Args를 사용한 데이터 전달**

### 방식별 장단점

1. **Bundle과 FragmentManager**:
    - **장점**: 직관적이고 간단하게 데이터 전달이 가능합니다.
    - **단점**: 데이터를 전달받은 `Fragment`에서 `ViewModel`에 데이터를 전달하고, `ViewModel`에서 UI 상태를 업데이트해야 하는 번거로움이 있습니다.
2. **Fragment Result API**:
    - **장점**: 명확한 데이터 전달 방식으로, 전달한 데이터를 안전하게 받을 수 있습니다.
    - **단점**: Fragment간의 데이터 전달 과정이 다소 복잡할 수 있습니다.
3. **공통의 ViewModel (SharedViewModel)**:
    - **장점**: 여러 `Fragment`가 동일한 `ViewModel` 인스턴스를 공유함으로써, 하나의 `Fragment`에서 변경된 데이터나 상태가 다른 `Fragment`에서도 즉시 반영됩니다. 이는 데이터와 상태 관리의 일관성을 보장합니다.
    - **단점**: `ViewModel`의 라이프사이클에 대한 이해가 필요하며, 일부 경우에는 `Activity`가 종료될 때 `ViewModel`이 사라지므로 주의가 필요합니다.
4. **Safe-Args (Jetpack Navigation)**:
    - **장점**: 데이터 전달 시 타입 안전성을 보장합니다. 데이터 타입이 다를 경우 컴파일 에러를 발생시켜 의도하지 않은 상황을 방지합니다.
    - **단점**: 설정과 이해해야 할 기능이 많아 환경 세팅이 복잡할 수 있습니다.

`SharedViewModel`을 사용하여 하나의 `Activity`에서 여러 `Fragment` 간에 데이터를 효율적으로 공유할 수 있습니다. `ViewModel`은 `Activity`의 라이프사이클보다 더 오래 살아남기 때문에, 공통의 `Activity` `ViewModel`을 사용하여 안전하게 데이터 전달과 상태 관리를 할 수 있습니다.<br><br><br>


  ### ▶ CloudFunction을 사용한 이유

Cloud Functions는 서버 관리와 유지보수를 클라우드 서비스 제공업체에 맡기고, 개발자는 코드 작성에만 집중할 수 있는 서비스입니다. 이 서비스는 **이벤트 트리거** 기능을 통해 Firebase에서 제공하는 서비스의 데이터 변화를 감지하고, 해당 이벤트에 반응하여 개발자가 미리 작성해둔 함수를 자동으로 실행합니다. 이를 통해 개발자는 다양한 상황에 맞춰 필요한 기능을 효율적으로 구현할 수 있습니다.

**FireBase Cloud Function을 사용한 기능**

**알림 제공**:  새로운 메시지가 도착하거나 특정 이벤트가 발생했을 때 사용자에게 푸시 알림을 전송할 수 있습니다. 이를 통해 게시글에 댓글이 달릴 경우 알림 제공 기능을 구현했습니다.

**데이터베이스 정리 및 유지보수**: 데이터베이스에 새로운 데이터가 추가되면 해당 데이터를 처리하거나 필요 없는 데이터를 정리할 수 있습니다. 이를 통해 프로필 이미지 변경 시 댓글, 게시글 등에 프로필 이미지 변경 같은 기능을 구현했습니다.

**탈퇴한 사용자 대응**:  사용자가 탈퇴한 경우 사용자 정보, 토큰, uid 값 등을 정리 해야 하는 과정을 단순히 앱에서 처리하기에는 firebase 정책 상 10분 이내의 재 로그인 사용자에게 만 허용이 되었고, 탈퇴를 하는 과정에서 재 인증을 요청하는 UI/UX는 좋지 않다고 생각해서 이러한 방향성으로 구현하였습니다.

  <br><br><br><br><br><br>



  
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
    - `Hilt()`의 SingletonComponent::class는 애플리케이션 생성 생명주기에서 인스턴스를 생성하지만, 항상 동일한 인스턴스를 제공하지는 않는다는 사실을 알게 되었습니다. 이를 해결하기 위해 @Singleton 애너테이션을 추가하여 하나의 인스턴스만 생성되도록 수정하였습니다. 이로써 Hilt를 사용할 때 `@Singleton` 애너테이션을 통해 단일 인스턴스를 보장할 수 있다는 점을 깨달았습니다.

      <br><br><br><br><br><br>

  
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

<br><br><br><br><br><br>

  
# ☁ 앞으로의 계획
![앞으로의 계획](https://github.com/Stop-smoke/KekKek/assets/141006937/37d510a2-3e40-428e-933c-9e816b8dc507)

- 커뮤니티 유저끼리의 팔로우/팔로워 기능을 구현하고, 서로의 프로필에서 확인할 수 있도록 구현할 계획입니다.
- 다양한 언어(영어 등)에 대응하는 언어 설정을 추가할 계획입니다. 
- 다크모드에 대응하여, 테마 설정을 추가할 계획입니다.
- 로그인을 하지 않아도 커뮤니티와 어플을 전체적으로 확인해볼 수 있도록 게스트모드를 추가할 계획입니다.
- 앱 내 광고를 추가할 계획입니다.
- UI/UX를 보다 개선하고, 사용자 친화적인 방향으로 바꾸어나갈 예정입니다.

<br><br><br><br><br><br>

### 켁켁이는 계속 됩니다 ... ☁ 
