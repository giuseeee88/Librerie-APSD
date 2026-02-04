# 📚 Java Data Structures Library

Progettazione e implementazione di una libreria di **strutture dati fondamentali** in Java, sviluppata seguendo i principi della programmazione a oggetti (OOP) e i diversi livelli di astrazione.

---

## 🛠️ Caratteristiche del Progetto

Il progetto si focalizza sulla creazione di strutture robuste e riutilizzabili, organizzate attraverso:

* **Interfacce:** Per definire il comportamento (contratto) delle strutture.
* **Classi Astratte:** Per fattorizzare il codice comune e ridurre la ridondanza.
* **Classi Concrete:** Implementazioni specifiche pronte all'uso.

### Strutture Implementate
* **Set & Sorted Set:** Gestione di insiemi di elementi unici (anche ordinati).
* **Linked List:** Liste concatenate dinamiche.
* **Stack (Pila):** Gestione LIFO (*Last-In-First-Out*).
* **Queue (Coda):** Gestione FIFO (*First-In-First-Out*).

> [!IMPORTANT]
> **Generics:** Tutte le strutture utilizzano i **Java Generics** (`<T>`), rendendo la libreria totalmente indipendente dal tipo di dato contenuto e garantendo il *type-safety* a tempo di compilazione.



---

## 🚀 Guida all'uso e Testing

Per verificare il corretto funzionamento delle librerie, sono disponibili dei test predefiniti nella cartella `zapsdtest`.

### Eseguire i Test
Apri una sessione **PowerShell** nella directory principale del progetto (`apsd`) ed esegui il seguente comando:

```powershell
powershell.exe -ExecutionPolicy Bypass -File .\winbuild.ps1 runtest
