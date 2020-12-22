## Service can execute script-function in Javascript or Groovy 

### DONE LIST:
* Execution single result script or batch
* Persisting results to db if results exists(calculated by content payload) return from db
* Security with roles that persists in DB (H2 but can be replaced by other because of Spring Data)
* Swagger
* Spring Actuator with Micrometer endpoints (NO IMAGE but can be finished)
* Possibility to choose script engine by property Groovy or Nashorn 
* Integration Tests that do test from API to DB
* Documented Architecture in folder
* Logging with different levels
* Custom Error Response handling

### TECH DEBT:
* Improve unit test coverage
* Add another DataSource for Prod profile like postgres
* Beautify api  payload as list etc)
* Move security and roles to other service (I have no plans to build monolith)
* Add Docker or compose with metric tracking

#### how to run: build and run main class
goto: localhost:8080

Login: admin or user, password: pass (will redirect to swagger)

Set flag for Script engine: 
  * engine.groovy=true
  * engine.javascript=false 
    
#### All results persisted in DB, in case of same execution it will get result from db 

#### DB console http://localhost:8080/h2-console/ (access under admin creds),

#### Execution request flow 

        ┌─┐                                                                          ,.-^^-._         
        ║"│                                                                         |-.____.-|        
        └┬┘                                                                         |        |        
        ┌┼┐                                                                         |        |        
         │                                       ┌─────────────┐                    |        |        
        ┌┴┐                                      │ScriptService│                    '-.____.-'        
      Client                                     └──────┬──────┘                    Database          
        │             Authentication Request            │                              │              
        │ ──────────────────────────────────────────────>                              │              
        │                                               │                              │              
        │                                               │ Check Roles username and pass│              
        │                                               │  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ >              
        │                                               │                              │              
        │ Authentication Response(redirect swagger page)│                              │              
        │ <─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─                               │              
        │                                               │                              │              
        │            Script Execution Request           │                              │              
        │ ──────────────────────────────────────────────>                              │              
        │                                               │                              │              
        │                                               │         check results        │              
        │                                               │  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ >              
        │                                               │                              │              
        │                                               │                              │              
        │                              ╔══════╤═════════╪══════════════════════════════╪═════════════╗
        │                              ║ ALT  │  no previous executions case           │             ║
        │                              ╟──────┘         │                              │             ║
        │                              ║                │─ ─ ┐                         │             ║
        │                              ║                │    | execute script          │             ║
        │                              ║                │< ─ ┘                         │             ║
        │                              ║                │                              │             ║
        │                              ║                │         save results         │             ║
        │                              ║                │  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ >             ║
        │                              ╚════════════════╪══════════════════════════════╪═════════════╝
        │                                               │                              │              
        │                                               │          get results         │              
        │                                               │  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ >              
        │                                               │                              │              
        │                                               │        return results        │              
        │                                               │ <─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─              
        │                                               │                              │              
        │               Execution Results               │                              │              
        │ <─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─                               │              
      Client                                     ┌──────┴──────┐                    Database          
        ┌─┐                                      │ScriptService│                     ,.-^^-._         
        ║"│                                      └─────────────┘                    |-.____.-|        
        └┬┘                                                                         |        |        
        ┌┼┐                                                                         |        |        
         │                                                                          |        |        
        ┌┴┐                                                                         '-.____.-'        
