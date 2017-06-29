package encina.trabajofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.BitmapFactory;



import encina.trabajofinal.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity {

    private int id_usuario;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        dbHelper = new DatabaseHelper(this);
        id_usuario = getIntent().getExtras().getInt("id");
        agregarItems();

        //instancio el shared preferences para utilizar mas adelante


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       /*/         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();   /*/
                Intent agregarMomento = new Intent(ItemListActivity.this,AgregarMomento.class);
                agregarMomento.putExtra("id",id_usuario);
                startActivity(agregarMomento);
                            //Se acomienza el activity pasandole el intent y una constante
            }
        });

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);



        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item){
        SharedPreferences preferencias = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        switch (item.getItemId()){
            case R.id.action_cerrarSesion:
                //Quita las preferencias del logeo automatico del usuario y redirecciona a la pagina del login
                SharedPreferences.Editor editor = preferencias.edit();
                editor.remove("user");
                editor.remove("pass");
                editor.remove("id");
                editor.commit();
                Intent login = new Intent(ItemListActivity.this, LoginActivity.class);
                startActivity(login);
                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void agregarItems(){
        DummyContent.ITEMS.clear();              //Limpio el array de items cada vez que se vuelve a cargar el ItemListActivity
                                                //Si no lo limpiara, al ser estatico el array cada vez que se ejecuta este intent
                                               //A los items que ya estan se les agregan los mismos pero se suman al array
        SQLiteDatabase bd = dbHelper.open();
        String consulta="SELECT * from Momentos WHERE id_usuario='"+id_usuario+"';";
        Cursor fila = bd.rawQuery(consulta,null);
        if (fila !=null) {             //Si la consulta existe quiere decir que fila no va a ser null
            fila.moveToFirst();         //Me muevo en los registros
            for (int i = 0; i < fila.getCount(); i++) {        //Recorro cada Momento y lo agrego al array de ITEMS
                DummyContent.addItem(new DummyContent.DummyItem(fila.getInt(0),fila.getBlob(1),fila.getString(2),fila.getString(3),fila.getString(4),fila.getString(5),fila.getInt(6)));
                fila.moveToNext();
            }
            fila.close();
        }
        dbHelper.close();


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }
        //Indica que los objetos ViewHolder(items) se van a mostar en el layout item_list_content
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //Indica que los objetos viewHolder se van a mostrar en el layout item_list_content.xml
            View view = LayoutInflater.from(parent.getContext())                //O algo asi es algo que tengo que ver
                    .inflate(R.layout.item_list_content, parent, false);              //Aca hace algo con el layout item_list_content.xml
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {   //Recibe como parametro la clase ViewHolder, que va a tener todos los parametros del item
            holder.mItem = mValues.get(position);                               //En la variable mItem se guarda la posicion de la lista de items que se trajo
            holder.mIdView.setText(String.valueOf(mValues.get(position).id));                   //En mIdView se guarda el id del item traido y se pega en un texto
            Bitmap imagen = BitmapFactory.decodeByteArray(mValues.get(position).foto, 0, mValues.get(position).foto.length);
            holder.mFotoView.setImageBitmap(imagen);

         //   holder.mFotoView.setImageResource(R.drawable.descarga2);
            holder.mContentView.setText(mValues.get(position).nota);        //Aca se tra el id y la nota del item y se pegan en otro texto



            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.id));
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);    //Redirecciono a la actividad ItemDetailActivity
                        //El putExtra es algo asi como clave: valor...Variable: valor
                        //En este caso se envia la variable ARG_ITEM_ID = id del item en cuestion (el seleccionado)
                        //Al llegar a la otra actividad, esta va a reemplazar a la variable ya definida ARG_ITEM_ID, por lo que va a tener este nuevo valor del id del ITEM
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID,String.valueOf(holder.mItem.id));  //Le mando como parametro la variable ARG_ITEM_ID con el valor id del ITEM
                        intent.putExtra("id",id_usuario);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public final ImageView mFotoView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
                mFotoView = (ImageView) view.findViewById(R.id.imageView);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
