啟動方式
docker-compose up -d

關閉方式
docker-compose down

---------------------------------------------------

API 說明
1. triggerInsertTestData
    - 說明: 間隔 5 - 10 秒插入 1 筆資料, 直至插入 100000 筆測試資料到資料庫, 或收到停止訊號
    - 方法: POST
    - 範例: http://localhost:8080/triggerInsertTestData
    - 參數: 無
    - 範例回傳: "開始插入測試資料。"

2. stopInsertTestData
    - 說明: 停止插入測試資料
    - 方法: POST
    - 範例: http://localhost:8080/stopInsertTestData
    - 參數: 無
    - 範例回傳: "停止插入測試資料的請求已發出。"

2. notificationsAfter
   - 說明: 取得指定時間之後的通知
   - 方法: GET
   - 範例: http://localhost:8080/notificationsAfter?timestamp=1756351245390
   - 參數:
     - timestamp: 指定的時間戳 (毫秒)
   - 範例回傳: JSON 陣列, 包含指定時間之後的通知資料
     - [
         {
            "timestamp": 1756351245392,
            "message": "備份系統檢查完成"
         },
         {
            "timestamp": 1756351245394,
            "message": "促銷活動開始"
         },
         {
            "timestamp": 1756351245399,
            "message": "系統維護完成"
         }
       ]