啟動方式
docker-compose up -d

關閉方式
docker-compose down

---------------------------------------------------

API 說明
1. triggerInsertTestData
    - 說明: 間隔 5 - 10 秒, 插入 100 筆測試資料到資料庫
    - 方法: POST
    - 範例: http://localhost:8080/triggerInsertTestData
    - 參數: 無
    - 範例回傳: "Test data insertion completed."

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