Service can execute script-function in Javascript or Groovy 

DONE LIST:
1) Execution single result script or batch
2) Persisting results to db if results exists(calculated by content payload) return from db
3) Security with roles that persists in DB (H2 but can be replaced by other because of Spring Data)
4) Swagger
5) Spring Actuator with Micrometer endpoints (NO IMAGE but can be finished)
6) Possibility to choose script engine by property Groovy or Nashorn 
7) Integration Tests that do test from API to DB
8) Documented Architecture in folder
9) Logging with different levels
10)Custom Error Response handling

TECH DEBT:
1) Improve unit test coverage
2) Add another DataSource for Prod profile like postgres
3) Beautify api  payload as list etc)
4) Move security and roles to other service (I have no plans to build monolith)
5) Add Docker or compose with metric tracking

how to run: build and run main class
goto: localhost:8080

Login: admin or user, password: pass 

Set flag for language: 
    - engine.groovy=true
    - engine.javascript=false 
    
All results persisted in DB, in case of same execution it will get result from db 

DB console http://localhost:8080/h2-console/ (access under admin creds),

Execution request flow 

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
