# ShiftMaker
Progetto personale per creazione turni per un reparto ospedaliero

Il progetto prevede, una generazione dei turni mensili sulla base di dati letti da un excel compilato.
Questi dati sono le persone a disposizione, le varie indisponibilità ed eventuali turni già schedulati.

Per la generazione ci si appoggia ad un database Mysql per la persistenza e per aiutarsi nella generazione del turno mensile
si utilizza un database inMemory H2.
Vi sono due moduli da lanciare in parallelo uno per la generazione dei turni l'altro che leggendoli dal database genera automaticamente le statistiche e il report finali


Per estrarre il miglior turno generato viene calcolato uno score basato sulla deviazione standard delle varie grandezze.

