package apsd.classes.containers.collections.abstractcollections;

import apsd.classes.containers.collections.abstractcollections.bases.WSetBase;
import apsd.classes.containers.collections.concretecollections.VList;
import apsd.classes.utilities.Box;
import apsd.classes.utilities.Natural;
import apsd.interfaces.containers.base.IterableContainer;
import apsd.interfaces.containers.base.TraversableContainer;
import apsd.interfaces.containers.collections.Chain;
import apsd.interfaces.containers.collections.Set;
import apsd.interfaces.containers.iterators.BackwardIterator;
import apsd.interfaces.containers.iterators.ForwardIterator;
import apsd.interfaces.traits.Predicate;

/** Object: Wrapper set implementation via chain (VList). */
public class WSet<Data> extends WSetBase<Data, VList<Data>> {

    /* --- Constructors --- */

    public WSet() {
        super();
        // ChainAlloc viene chiamato automaticamente dal costruttore di WSetBase se previsto,
        // oppure chn è inizializzato qui tramite la chiamata implicita a ChainAlloc.
    }

    public WSet(Chain<Data> chn) {
        super();
        // Copia elementi dalla catena fornita assicurando l'unicità
        ForwardIterator<Data> it = chn.FIterator();
        while (!it.IsValid()) {
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    public WSet(TraversableContainer<Data> con) {
        super();
        this.InsertAll(con);
    }

    public WSet(Chain<Data> chn, TraversableContainer<Data> con) {
        this(chn); // Inizializza con la catena
        this.InsertAll(con); // Aggiunge il contenitore
    }

    /* --- Protected Allocation --- */

    @Override
    protected VList<Data> ChainAlloc() {
        return new VList<Data>();
    }

    /* --- Basic Operations --- */

    @Override
    public boolean Exists(Data dat) {
        // Delega alla lista sottostante
        return chn.Exists(dat);
    }

    @Override
    public boolean Insert(Data dat) {
        // In un Set, inseriamo solo se l'elemento NON esiste già
        if (!chn.Exists(dat)) {
            // Usiamo Insert o PushBack della VList
            chn.Insert(dat); 
            return true;
        }
        return false;
    }

    @Override
    public boolean Remove(Data dat) {
        // Delega alla lista sottostante
        return chn.Remove(dat);
    }

    @Override
    public void Clear() {
        chn.Clear();
    }

    @Override
    public boolean IsEmpty() {
        return chn.IsEmpty();
    }

    @Override
    public Natural Size() {
        return chn.Size();
    }

    /* --- Set Theory Operations --- */

    @Override
    public void Union(Set<Data> set) {
        // A U B: Aggiungi tutti gli elementi di B in A
        // Insert gestisce già l'unicità
        ForwardIterator<Data> it = set.FIterator();
        while (!it.IsValid()) {
            this.Insert(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Difference(Set<Data> set) {
        // A - B: Rimuovi da A tutti gli elementi presenti in B
        ForwardIterator<Data> it = set.FIterator();
        while (!it.IsValid()) {
            this.Remove(it.GetCurrent());
            it.Next();
        }
    }

    @Override
    public void Intersection(Set<Data> set) {
        // A intersect B: Mantieni in A solo gli elementi presenti anche in B.
        // Strategia: Troviamo gli elementi da rimuovere (quelli che sono in A ma non in B)
        // per evitare problemi di concorrenza durante l'iterazione.
        
        VList<Data> toRemove = new VList<>();
        ForwardIterator<Data> it = this.FIterator();
        
        while (!it.IsValid()) {
            Data current = it.GetCurrent();
            if (!set.Exists(current)) {
                toRemove.Insert(current);
            }
            it.Next();
        }
        
        // Rimuoviamo gli elementi identificati
        this.RemoveAll(toRemove);
    }

    @Override
    public boolean IsEqual(IterableContainer<Data> set) {
        // Due insiemi sono uguali se hanno la stessa dimensione e gli stessi elementi.
        // Nota: Assumiamo che 'set' sia un Set o un container con semantica simile.
        
        // 1. Controllo Dimensione (Ottimizzazione)
        // Dobbiamo convertire Natural in int/long per il confronto, o usare equals se Natural lo supporta.
        // Assumo qui un confronto standard o conversione implicita/esplicita a intero.
        if (this.Size().compareTo(set.Size()) != 0) { 
            return false;
        }

        // 2. Controllo Sottoinsieme
        // Ogni elemento di questo Set deve esistere nell'altro container.
        ForwardIterator<Data> it = this.FIterator();
        while (!it.IsValid()) {
            // Nota: IterableContainer potrebbe non avere Exists efficiente, ma usiamo l'interfaccia disponibile.
            // Se IterableContainer non ha Exists, bisognerebbe iterarlo, ma di solito Set lo estende.
            // Qui assumiamo di poter controllare l'esistenza. Se set non ha Exists, il controllo è più complesso.
            // Generalmente in queste librerie si fa un cast o si usa un iteratore.
            
            // Approccio generico: cerchiamo it.Current() in 'set'
            boolean found = false;
            ForwardIterator<Data> otherIt = set.FIterator();
            while(!otherIt.IsValid()){
                if(otherIt.GetCurrent().equals(it.GetCurrent())){
                    found = true;
                    break;
                }
                otherIt.Next();
            }
            
            if (!found) return false;
            
            it.Next();
        }

        return true;
    }

    /* --- Bulk Operations --- */

    @Override
    public boolean InsertAll(TraversableContainer<Data> container) {
        // Usiamo un Box per catturare lo stato "modificato" all'interno della lambda
        final Box<Boolean> modified = new Box<>(false);
        
        container.TraverseForward(
            (Data dat) -> {
                // Proviamo a inserire l'elemento corrente
                if (this.Insert(dat)) {
                    modified.Set(true); // Se inserito, segniamo che c'è stata una modifica
                }
                return false; // Restituiamo false per CONTINUARE l'attraversamento su tutti gli elementi
            }
        );
        
        return modified.Get();
    }

    @Override
    public boolean InsertSome(TraversableContainer<Data> container) {
        // Sinonimo di InsertAll
        return InsertAll(container);
    }

    @Override
    public boolean RemoveAll(TraversableContainer<Data> container) {
        // Stessa logica di InsertAll, ma chiamiamo Remove
        final Box<Boolean> modified = new Box<>(false);
        
        container.TraverseForward(
            (Data dat) -> {
                if (this.Remove(dat)) {
                    modified.Set(true);
                }
                return false; // Continua ad attraversare
            }
        );
        
        return modified.Get();
    }

    @Override
    public boolean RemoveSome(TraversableContainer<Data> container) {
        // Sinonimo di RemoveAll
        return RemoveAll(container);
    }

    /* --- Iterators & Traversal --- */

    @Override
    public ForwardIterator<Data> FIterator() {
        return chn.FIterator();
    }

    @Override
    public BackwardIterator<Data> BIterator() {
        return chn.BIterator();
    }

    @Override
    public boolean TraverseForward(Predicate<Data> predicate) {
        return chn.TraverseForward(predicate);
    }

    @Override
    public boolean TraverseBackward(Predicate<Data> predicate) {
        return chn.TraverseBackward(predicate);
    }
}