純後端執行方式

    僅執行後端
    docker run -d -p 8080:8080 --name my-app-container my-app
    
    暫停後端
    docker stop my-app-container
    
    恢復後端
    docker start my-app-container
    
    關閉並刪除後端
    docker rm my-app-container

---------------------------------------------------
前後端合併執行方式

    啟動前+後端
    docker-compose up -d
    
    暫停前+後端
    docker-compose stop
    
    恢復前+後端
    docker-compose start
    
    關閉並刪除前+後端
    docker-compose down

---------------------------------------------------

Polling API 說明
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

---------------------------------------------------

Long Polling API 說明





回傳 CompletableFuture 與 DeferredResult 比較

|  特性   | `CompletableFuture`  | `DeferredResult` |
|  ----  | :----:  | :---: |
| 層級  | Java 原生 | Spring 特有 |
| 回傳控制  | 自動完成後返回 | 手動呼叫 `setResult()` |
| 彈性  | 一般 | 更高（支援 timeout、error 等） |
| 簡潔度  | 高 | 需要多些代碼 |
| 適合場景  | 簡單非同步操作 | 複雜流程、事件驅動 |


如果你需要更強的非同步/反應式支持，甚至可以考慮：
WebFlux + Mono/Flux（反應式編程，非阻塞）
Callable 或 WebAsyncTask（也是 Spring MVC 的 async 支援）