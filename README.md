# 📚 Librerie per Analisi e Progettazione di Strutture Dati

Progettazione e implementazione di librerie di **strutture dati fondamentali** in Java, sviluppate in linguaggio Java seguendo i principi della programmazione a oggetti (OOP) e i diversi livelli di astrazione.

---

## 🛠️ Caratteristiche del Progetto

Il progetto si focalizza sulla creazione di strutture robuste e riutilizzabili, organizzate attraverso:

* **Interfacce:** Per definire il comportamento delle strutture.
* **Classi Astratte:** Per fattorizzare il codice comune e ridurre la ridondanza.
* **Classi Concrete:** Implementazioni specifiche pronte all'uso.

### Strutture Implementate
* **Set & Sorted Set:** Gestione di insiemi di elementi unici (anche ordinati).
* **Linked List:** Liste concatenate dinamiche.
* **Stack (Pila):** Gestione LIFO (*Last-In-First-Out*).
* **Queue (Coda):** Gestione FIFO (*First-In-First-Out*).

### Informazioni sul diagramma delle classi/interfacce
La struttura del progetto è organizzata secondo il diagramma delle classi/interfacce presente nel file `Diagrams.pdf`
Nelle varie classi e nelle varie interfacce sono riportati dei metodi con determinati colori, i quali vanno interpretati nel seguente modo:
* Nero - Metodo astratto
* Verde - Metodo con implementazione default
* Blu - Metodo reiimplementato (override)
* Rosso - Metodo reiimplementato astratto
Mentre nelle tabelle i metodi di una determinata classe che hanno le spunte devono essere reiimplementati

> [!IMPORTANT]
> Le strutture dati sono progettate per essere utilizzate con dati generici ovvero interi, stringhe ecc.

---

## 🚀 Guida all'uso e Testing

Per verificare il corretto funzionamento delle librerie, sono disponibili dei test predefiniti nella cartella `zapsdtest` mediante l'utilizzo del framework JUnit5

### Eseguire i Test
Apri una sessione **PowerShell** nella directory principale del progetto (`apsd`) ed esegui il seguente comando:

```powershell
powershell.exe -ExecutionPolicy Bypass -File .\winbuild.ps1 runtest
```

### Eliminare i file .class creati dal compilatore Java dopo la compilazione del codice
Apri una sessione **PowerShell** nella directory principale del progetto (`apsd`) ed esegui il seguente comando:

```powershell
Get-ChildItem -Recurse -Filter *.class | Remove-Item -Force
```
