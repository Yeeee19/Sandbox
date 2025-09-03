<h3> 純後端執行方式 </h3>

    建立映像檔 (當後端程式更新時, 關閉並刪除既有容器, 執行新的映像檔)
    docker build -t my-app .

    僅執行後端
    docker run -d -p 8080:8080 --name my-app-container my-app
    
    暫停後端
    docker stop my-app-container
    
    恢復後端
    docker start my-app-container
    
    關閉並刪除後端
    docker rm my-app-container

---------------------------------------------------
<h3> 前後端合併執行方式 </h3>

    啟動前+後端
    docker-compose up -d
    
    暫停前+後端
    docker-compose stop
    
    恢復前+後端
    docker-compose start
    
    關閉並刪除前+後端
    docker-compose down

---------------------------------------------------
<h3> 觸發測試資料 API </h3>

1. triggerInsertTestData
    - 說明: 間隔 5 - 10 秒插入 1 筆資料, 直至插入 100000 筆測試資料到資料庫, 或收到停止訊號
    - 方法: POST
    - 範例: http://localhost:8080/triggerInsertTestData
    - 參數: 無
    - 範例回傳: "結束插入測試資料。"


2. stopInsertTestData
    - 說明: 停止插入測試資料
    - 方法: POST
    - 範例: http://localhost:8080/stopInsertTestData
    - 參數: 無
    - 範例回傳: "停止插入測試資料的請求已發出。"

---------------------------------------------------
<h3> Polling API </h3>

1. polling
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
<h3> Long Polling API </h3>

1. longPolling
    - 說明: 異步取得新通知
    - 方法: GET
    - 範例: http://localhost:8080/longPolling
    - 參數: 無
    - 範例回傳: JSON 陣列
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
    - 行為:
      - 若有新通知, 立即回傳包含新通知的 JSON 陣列
      - 若無新通知, 等待最多 30 秒, 503 狀態碼表示逾時


2. longPolling-mono
    - 說明: 使用 WebFlux 異步取得新通知
    - 方法: GET
    - 範例: http://localhost:8080/longPolling/mono
    - 參數: 無
    - 範例回傳: JSON 陣列
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
    - 行為:
      - 若有新通知, 立即回傳包含新通知的 JSON 陣列
      - 若無新通知, 等待最多 30 秒, 503 狀態碼表示逾時

<h3> 各種非同步處理方式比較 </h3>

| 特性/方式             | `Callable` | `WebAsyncTask` | `DeferredResult`     | `CompletableFuture`        | `WebFlux (Mono/Flux)`   |
|:------------------| :----------: | :--------------: | :--------------------: | :--------------------------: | :-----------------------: |
| 所屬技術棧             | Spring MVC | Spring MVC     | Spring MVC           | Spring MVC                 | **Spring WebFlux**（反應式） |
| 是否阻塞 Servlet 線程   | ❌ 釋放主線程    | ❌ 釋放主線程        | ❌ 釋放主線程              | ❌ 釋放主線程                    | ✅ 非阻塞                   |
| 例外處理支持            | ✅          | ✅（支持逾時處理）      | ❌（需自定義）              | ❌（需自定義）                    | ✅（統一處理）                 |
| 超時處理              | ❌          | ✅              | ✅                    | ✅（結合超時邏輯）                  | ✅（內建機制）                 |
| 控制靈活性             | ❌ 簡單封裝     | ✅ 可設定逾時/錯誤回調   | ✅ 完全控制回應             | ✅ 更現代、更標準                  | ✅ 高靈活                   |
| 適用場景              | 簡單異步任務     | 複雜異步任務         | 外部事件觸發（如MQ、callback） | 與 CompletableFuture API 整合 | 高併發、非阻塞場景               |
| Spring Boot 支援版本  | ✅ MVC 預設支援 | ✅              | ✅                    | ✅（Spring 4+）               | ✅（需使用 WebFlux 模組）       |

---------------------------------------------------
<h3> Web Socket API </h3>

1. webSocket
    - 說明: 使用 WebSocket 取得新通知
    - 方法: WebSocket
    - 範例: ws://localhost:8080/webSocket
    - 參數: 無
    - 範例回傳: JSON 陣列, 包含指定時間之後的通知資料
      - "備份系統檢查完成"
      - "促銷活動開始"
   - 行為:
     - 若有新通知, 立即回傳新通知


---------------------------------------------------

<h3> 系統監控 </h3>

spring-boot-actuator + spring-boot-admin

    http://localhost:8080/



可考慮 grafana + prometheus 做更進階的監控

---------------------------------------------------

arthas