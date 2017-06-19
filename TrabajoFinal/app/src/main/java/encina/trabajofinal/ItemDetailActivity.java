package encina.trabajofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

    /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });/*/

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Crea el fragment detail (ItemDetailFragment) y lo agrega dentro de esta misma activudad
            // Usando utransaction fragment (dentro del contenedor del layout usa el fragment o sea (item_detail.xml)
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));         //Con el getIntent tomo el valor del parametro enviado desde el activity anterior
            //Recibi del activity ItemListActivity una variable ARG_ITEM_ID = valor id del item seleccionado, con el getIntent()
            //Con el getStringExtra tomo el valor de esta variable (el id, y se lo asigno a la variable ItemDetailFragment.ARG_ITEM_ID
            //que con el putString va a ser agregada en la variable arguments para luego ser enviado como parametro al fragment ItemDetailFragment
            ItemDetailFragment fragment = new ItemDetailFragment();                 //Creo una instancia del fragment ItemDetailFragment
            fragment.setArguments(arguments);                                       //Le paso el id del item al fragment
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)              //Meto el fragment dentro del contenedor que se encuentra en el layout activity_item_detail.xml
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                     //Metodo cuando se apreta en la flechita de la barra para volver hacia atras
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Este ID representa volver atras, en el caso de esta actividad es para el boton de arriba

            navigateUpTo(new Intent(this, ItemListActivity.class));             //Te hace volver a la clase anterior o sea ItemListActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
