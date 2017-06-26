package encina.trabajofinal;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import encina.trabajofinal.dummy.DummyContent;
import android.webkit.WebView;                     //Paquete para pegar algo de la web (el mapa)
import android.webkit.WebViewClient;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {                      //Con getArguments() retorna los elementos enviados o sea el id
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID)); //Busca el item por medio de la id en el ITEM_MAP y trae el item entero con todos sus valores
            Bundle arguments = new Bundle();
            arguments.putString(MapFragment.latitud, mItem.latitud);         //Con el getIntent tomo el valor del parametro enviado desde el activity anterior
            arguments.putString(MapFragment.longitud, mItem.longitud);
            //Recibi del activity ItemListActivity una variable ARG_ITEM_ID = valor id del item seleccionado, con el getIntent()
            //Con el getStringExtra tomo el valor de esta variable (el id, y se lo asigno a la variable ItemDetailFragment.ARG_ITEM_ID
            //que con el putString va a ser agregada en la variable arguments para luego ser enviado como parametro al fragment ItemDetailFragment

            MapFragment nMapFragment =new  MapFragment();
            nMapFragment.setArguments(arguments);
            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.linearFragmento, nMapFragment);
            fragmentTransaction.commit();




        /*/    MapFragment fragment =new  MapFragment();                 //Creo una instancia del fragment ItemDetailFragment
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.linearFragmento, fragment)              //Meto el fragment dentro del contenedor que se encuentra en el layout activity_item_detail.xml
                    .commit();  /*/

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Momento "+mItem.id);                  //Valor a modificar que muestra el titulo en el detalle del fragment
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);   //El layout asociado al fragment que se va a mostrar

        // Codigo para mostrar en la pantalla del fragment los valores del item en detalle
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.txtFecha)).setText(mItem.fecha);              //Valor a modificar que muestra los detalles de los items en el fragment
            ((TextView) rootView.findViewById(R.id.txtDescripcion)).setText(mItem.nota);              //Valor a modificar que muestra los detalles de los items en el fragment
 /*           WebView mapita= ((WebView) rootView.findViewById(R.id.mapa));            //Valor a modificar que muestra los detalles de los items en el fragment


            mapita.setWebViewClient(new WebViewClient());
            mapita.getSettings().setJavaScriptEnabled(true);
            mapita.getSettings().setSupportZoom(true);
            mapita.getSettings().setBuiltInZoomControls(true);
            mapita.getSettings().setDisplayZoomControls(false);
            //mapita.loadUrl("http://www.ole.com.ar/");
            mapita.loadUrl("http://maps.google.com/maps?" + "saddr=43.0054446,-87.9678884" + "&daddr=42.9257104,-88.0508355");
            //mapita.loadUrl("https://www.google.com/maps/@"+mItem.latitud+","+mItem.longitud+",15.46z");   /*/
        }

        return rootView;
    }
}
