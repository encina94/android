package encina.trabajofinal;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;
import android.widget.Toast;
//Paquetes para geolocalizacion
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;



import java.io.ByteArrayOutputStream;
//Paquetes para obtener fecha y modificarla a gusto
import java.text.DateFormat;                     //Para usar el tipo de dato DateFormat con el que se puede modificar la fecha usando el SimpleDateFormat
import java.text.SimpleDateFormat;              //Para poner de que manera se quiere ver la fecha (YY/NN/MMMM) o distintas formas
import java.util.Date;                          //Para usar el formato Date

/**
 * Created by encin_000 on 20/06/2017.
 */

public class AgregarMomento extends AppCompatActivity {

    private ImageView image;
    private Bitmap foto;
    private Button btnGuardar;
    private Button btnCancelar;
    private EditText nota;
    private DatabaseHelper dbHelper;
    private TextView fecha;
    private TextView lati;
    private TextView longi;
    //Variables para la fecha
    private Date fechaa;
    private DateFormat formatoFecha;
    //Estos datos van  a ser harcodeados para guardarlos en la bd (Hasta que aprenda a usar la geolocalizacion

    double latitud,longitud;
    //private String latitud = "-34.7849226";
    //private String longitud = "-58.2642679";


    private LocationManager locationManager = null;
    private MyLocationListener locationListener = null;



    private int user = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_momento);
        image = (ImageView) findViewById(R.id.foto);
        nota = (EditText) findViewById(R.id.textDescripcion);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar= (Button) findViewById(R.id.btnCancelar);
        fecha = (TextView) findViewById(R.id.textFecha);
        lati = (TextView) findViewById(R.id.textLatitud);
        longi = (TextView) findViewById(R.id.textLongitud);

        dbHelper = new DatabaseHelper(this);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);   //Se abre un activity que permite capturar una imagen y la guarda en buffer
        startActivityForResult(cameraIntent,0);

        btnGuardar.setOnClickListener(new View.OnClickListener(){   //Asocio un manejador de eventos para el boton (listener)
            public void onClick(View v){
                guardarDatos();
                Intent listActivity = new Intent(AgregarMomento.this, ItemListActivity.class);
                startActivity(listActivity);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                descartar();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  //Cuando la camara saca una foto se ejecuta este codigo
//        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK){
            //Obtengo la foto
            foto = (Bitmap)data.getExtras().get("data");   //Tomo la foto que se tomo de la camara, la extraigo del buffer y la guardo en la bariable
            image.setImageBitmap(foto);                    //Pongo la foto en el ImageView para que se vea en pantalla
            //Obtengo la fecha
            //Seteo la fecha automatica
            fechaa = new Date();
            formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecha.setText(formatoFecha.format(fechaa));
            //Obtengo las coordenadas por gps
            obtenerLocalizacion();

        }
    }

    private void obtenerLocalizacion(){         //Metodo para instanciar el MyLocationListener y obtener coordenadas
        locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE); //Provee acceso al servicio de localizacion, no se instancia directamente sino que se recupera la instancia de esta manera
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){   //Si el gps esta habilitado entonces obtengo las coordenadas
            locationListener = new MyLocationListener();
            //Confirma si los permisos para acceder al gps fueron garantizados, esto debe hacerse porque sino el codigo de locationManager no funciona.
            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, locationListener);




        }else{
            Toast.makeText(getBaseContext(), "El GPS se encuentra deshabilitado",Toast.LENGTH_SHORT).show();
            //Obligar a activar el gps
        }

    }

    private void guardarDatos(){
        ContentValues registro = new ContentValues();   //contenido de los valores que se van a guardar
        //datos a guardar en las filas determinadas
        ByteArrayOutputStream stream = new ByteArrayOutputStream();     //Clase para poder transformar la foto de bitMap a un array de Bytes
        foto.compress(Bitmap.CompressFormat.JPEG, 100, stream);          //transformo la foto guardada en BitMap en formato JPEG en un array BYTE
        byte[] byteArray = stream.toByteArray();                      //El metodo toByteArray() crea una matriz de bytes recien asignadas (dentro va a estar la imagen bitmap que fue pasada a matriz de bytes y luego debe ser decodificada)
        registro.put("foto", byteArray);
        registro.put("nota", nota.getText().toString());
        registro.put("fecha", fecha.getText().toString());
        registro.put("latitud",latitud);
        registro.put("longitud",longitud);
        registro.put("id_usuario",1);

        SQLiteDatabase bd = dbHelper.open();           //abro la bd

        //Aca inserto todos los datos
        bd.insert("Momentos", null, registro);           //Inserto los datos guardados en registro en la tabla Momentos

        //cierro la consulta de prueba
        dbHelper.close();

        Toast.makeText(this, "El momento fue publicado", Toast.LENGTH_LONG).show();

    }

    private void descartar(){
        SQLiteDatabase bd = dbHelper.open();
        String consulta = "SELECT * FROM Momentos WHERE id= '" + user + "';";
        //Si el usuario no existe se dispara una excepcion, por eso se usa el try y el catch
        try {
            Cursor fila = bd.rawQuery(consulta, null);
            if (fila.moveToNext()) {                   //si existe un usuario en la bd con el mismo nombre que el ingresado, se prosigue, sino se dispara una excepcion
                String userBd = fila.getString(2);
            }
        }catch(SQLException e){                        //Al no existir usuario en bd, se dispara una excepcion y es capturada

            bd.close();

        }
    }

    public class MyLocationListener implements LocationListener{

    //    public double latitud, longitud;

        @Override
        public void onLocationChanged(Location loc){
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            lati.setText(String.valueOf(latitud));
            longi.setText(String.valueOf(longitud));

        }

        @Override
        public void onProviderDisabled(String provider){ //Se ejecuta cuando el GPS esta desabilitado
            //Poner una notificacion que indique que se deba activar el gps

        }
        @Override
        public void onProviderEnabled(String provider){ //Se ejecuta cuandl el GPS esta activado
            //Se podria poner un msj diciendo GPS ACTIVADO

        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
            // Este metodo se ejecuta cada vez que se detecta un cambio en el
            // status del proveedor de localizacion (GPS)
            // Los diferentes Status son:
            // OUT_OF_SERVICE -> Si el proveedor esta fuera de servicio
            // TEMPORARILY_UNAVAILABLE -> Temporalmente no disponible pero se
            // espera que este disponible en breve
            // AVAILABLE -> Disponible

        }

    }
}
