package encina.trabajofinal.dummy;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();  //Se crea una lista con objetos de tipo Dummy (items)

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();


    static{
        addItem(new DummyItem("1","loro","La hamburguesa mas rica del condado","15-03-2017","-34.7849226","-58.2151892"));
        addItem(new DummyItem("2","descarga2","Los simpsons","17-10-2017","-34.7769636","-58.2642679"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String foto;    //Tipo drawable para poder cargar una imagen
        public final String nota;
        public final String fecha;
        public final String latitud;
        public final String longitud;

        public DummyItem(String id, String foto, String nota, String fecha, String latitud, String longitud) {
            this.id = id;
            this.foto = foto;
            this.nota = nota;
            this.fecha = fecha;
            this.latitud = latitud;
            this.longitud = longitud;
        }

        @Override
        public String toString() {

            return id+""+nota;
        }
    }
}
