啟動方式
1. docker-compose up -d

API 說明
1. triggerInsertTestData
    - 說明: 觸發插入測試資料
    - 方法: POST
    - 範例: http://localhost:8080/triggerInsertTestData
    - 參數: 無
    - 回傳: String
    - 範例回傳: "Test data insertion completed."
    - 行為: 間隔1-3秒, 插入 100 筆測試資料到資料庫