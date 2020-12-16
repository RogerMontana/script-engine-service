Service can execute script-function in Javascript or Groovy 
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
