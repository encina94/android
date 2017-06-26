package encina.trabajofinal.dummy;



import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import encina.trabajofinal.DatabaseHelper;

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



    public static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(String.valueOf(item.id), item);   //Transformo el id int en string para ponerlo como clave
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final int id;
        public final byte[] foto;
        public final String nota;
        public final String fecha;
        public final String latitud;
        public final String longitud;
        public final int id_usuario;

        public DummyItem(int id, byte[] foto, String nota, String fecha, String latitud, String longitud, int id_usuario) {
            this.id = id;
            this.foto = foto;
            this.nota = nota;
            this.fecha = fecha;
            this.latitud = latitud;
            this.longitud = longitud;
            this.id_usuario = id_usuario;
        }

        @Override
        public String toString() {

            return id+""+nota;
        }
    }
}
