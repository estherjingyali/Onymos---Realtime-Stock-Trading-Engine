# Onymos---Realtime-Stock-Trading-Engine

A high concurrency system that simulate real-time stock transactions using multi-threads and lock-free linked lists to ensure system efficiency and scalability.

****
### Function
1. addOrder()
- Simulates real-time order by generating random transactions
- Executed multiple threads using newFixedThreadPool
- Order are stored in Lock-free LinkedList with CAS

2. matchOrder()
- Using newScheduledThreadPool to match orders periodically
- Allowed partial matches and updated remaining stock quantities
- Ensured synchronized execution of orders adding and matching

****
