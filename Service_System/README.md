Service is working on host : pos05169.pythonanywhere.com

## APIs

# GET

/api_root/client/ 전체 클라이언트

/api_root/client/<int>/ 클라이언트 pk 검색

/post/<int>/ 게시글 pk 검색

# POST

/api_root/client/ 클라이언트 생성

/api_root/video_stream/ 비디오 데이터 전달 (엣지 시스템에서 클라이언트 시스템으로)

/post/new/ 게시글 생성

/post/<int>/edit/ 게시글 수정

# PUT

/api_root/client/<int>/ 클라이언트 수정

# DELETE

/api_root/client/<int>/ 클라이언트 삭제
