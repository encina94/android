package encina.trabajofinal;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper  {

    private static final String DATABASE_NAME = "BD";   //nombre de la bd (string)
    //En la version 1 solo estaba creada la tabla Usuarios, se seteo la version 2 cuando se creo la tabla momentos
    private static final int DATABASE_VERSION = 3;      //version de la bd (puede actualizarse mediante metodos) (Ante cada modificacion que se le hace a la bd hay que aumentar la version para que seactualice)


    //Creacion de una tabla en la bd
    private static final String DATABASE_CREATE = "CREATE TABLE Usuarios (id integer primary key autoincrement not null, usuario varchar not null, email varchar not null, contrasenia varchar not null); ";
    private static final String DATABASE_CREATE_MOMENTOS = "CREATE TABLE Momentos (id integer primary key autoincrement not null, foto blob not null, nota varchar not null, fecha TEXT not null, latitud varchar not null, longitud varchar not null, id_usuario integer not null,FOREIGN KEY(`id_usuario`) REFERENCES `Usuarios`(`id`));";

    private SQLiteDatabase bd;                        //Instancia de la clase manejadora de la bd

    public DatabaseHelper(Context context) {              //Constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);   //Herencia del helper, contexto (contexto de accion para el helper, nombre de la bd, factory null, version de la bd)
    }

    public void onCreate(SQLiteDatabase db){ //metodo llamado automaticamente cuando se crea una instancia de SQLiteOpenHelper
        try{
            db.execSQL(DATABASE_CREATE);   //Ejecuta la query que crea la tabla de usuarios
            db.execSQL(DATABASE_CREATE_MOMENTOS);    //Ejecuta la query que crea la tabla de momentos
            db.execSQL ("PRAGMA foreign_keys = ON");             //Para habilitar las foreign keys
        } catch(SQLException e){           //Si se produce una excepcion de sql se imprime un metodo
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase db, int olvdVersion, int newVersion){ //metodo ejecutado si se identifica que se tiene una version antigua de la base de datos
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Momentos");
        onCreate(db);
    }

    public SQLiteDatabase open(){           //Metodo para poder escribir datos en la bd
        bd = this.getWritableDatabase();

        return bd;
    }

    public SQLiteDatabase read(){          //Metodo para leer datos de la bd
        bd = this.getReadableDatabase();

        return bd;
    }

    public void close(){       //Metodo para cerrar la bd
        bd.close();
    }
}
