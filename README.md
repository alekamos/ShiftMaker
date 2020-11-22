# ShiftMaker
Progetto personale per creazione turni per un reparto ospedaliero

Il progetto prevede, una generazione dei turni mensili sulla base di dati letti da un file di properties.
Questi dati sono le persone a disposizione, le varie indisponibilità ed eventuali turni già schedulati.

Per la generazione ci si appoggia ad un database Mysql per la persistenza e per aiutarsi nella generazione del turno mensile
si utilizza un database inMemory H2.


Per estrarre il miglior turno generato viene calcolato uno score basato sulla deviazione standard delle varie grandezze.

