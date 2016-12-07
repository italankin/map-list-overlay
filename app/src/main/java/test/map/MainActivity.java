package test.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.italankin.slidinglayout.SlidingLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final double START_LAT = 64.08438753;
    private static final double START_LONG = 12.49064574;
    private static final int COUNT = 25;

    private MapView mapView;
    private List<Item> data;
    private List<Item> filtered = new ArrayList<>();
    private RecyclerView list;
    private GoogleMap googleMap;
    private MySlidingLayout myLayout;
    private View imageClose;
    private View btnToggle;
    private int filter = -1;
    private View[] filterButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = (MySlidingLayout) findViewById(R.id.my_layout);
        data = createList();
        filtered.addAll(data);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new Adapter(this, filtered));
        list.addItemDecoration(new ListSeparator(this));
        list.addOnItemTouchListener(new ClickListener(this, new ClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView recyclerView, View view, int position) {
                if (googleMap != null) {
                    myLayout.hideOverlay();
                    Item item = filtered.get(position);
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(item.loc, 12);
                    googleMap.animateCamera(cu);
                }
            }
        }));
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        btnToggle = findViewById(R.id.btn_toggle);
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myLayout.isOverlayShowing()) {
                    myLayout.hideOverlay();
                } else {
                    myLayout.showOverlay();
                }
            }
        });
        imageClose = findViewById(R.id.image_arrow);
        myLayout.addOnDragProgressListener(new SlidingLayout.OnDragProgressListener() {
            @Override
            public void onDragProgress(float v) {
                btnToggle.setEnabled(v == 0 || v == 1);
                list.setEnabled(v == 1);
                imageClose.setRotation(180 * v);
            }
        });
        filterButtons = new View[]{
                findViewById(R.id.filter1),
                findViewById(R.id.filter2),
                findViewById(R.id.filter3)
        };
        for (View view : filterButtons) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateFilter(view);
                }
            });
        }
    }

    private void updateFilter(View view) {
        for (int i = 0; i < filterButtons.length; i++) {
            View v = filterButtons[i];
            if (v == view) {
                v.setSelected(!v.isSelected());
                filter = v.isSelected() ? i : -1;
            } else {
                v.setSelected(false);
            }
        }
        filterList();
    }

    private void filterList() {
        filtered.clear();
        if (filter == -1) {
            filtered.addAll(data);
        } else {
            for (Item item : data) {
                if (item.type == filter) {
                    filtered.add(item);
                }
            }
        }
        if (list != null && list.getAdapter() != null) {
            list.getAdapter().notifyDataSetChanged();
        }
        updateMap();
    }

    @Override
    public void onBackPressed() {
        if (myLayout.isOverlayShowing()) {
            myLayout.hideOverlay();
        } else {
            super.onBackPressed();
        }
    }

    private List<Item> createList() {
        List<Item> list = new ArrayList<>(COUNT);
        Random random = new Random();
        double v1 = START_LAT, v2 = START_LONG;
        for (int i = 0; i < COUNT; i++) {
            Item item = new Item();
            item.title = new LoremIpsum()
                    .setWordCount(3, 6)
                    .useCommas(true)
                    .useSentences(false)
                    .build();
            item.desc = new LoremIpsum()
                    .setWordCount(20, 50)
                    .useCommas(true)
                    .makeParagraphs(true)
                    .build();
            item.sub = new LoremIpsum()
                    .setWordCount(2, 3)
                    .useSentences(false)
                    .build();
            item.loc = new LatLng(v1 + 5 - 15 * random.nextDouble(),
                    v2 + 30 - 60 * random.nextDouble());
            item.type = random.nextInt(3);
            list.add(item);
        }
        return list;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateMap();
    }

    private void updateMap() {
        googleMap.clear();
        final LatLngBounds.Builder b = new LatLngBounds.Builder();
        for (Item item : filtered) {
            b.include(item.loc);
            MarkerOptions mo = new MarkerOptions()
                    .position(item.loc)
                    .snippet(item.sub)
                    .title(item.title);
            Marker marker = googleMap.addMarker(mo);
            marker.setTag(item);
            googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Item tag = (Item) marker.getTag();
                    myLayout.showOverlay();
                    list.smoothScrollToPosition(filtered.indexOf(tag));
                }
            });
        }
        if (filtered.size() < 2) {
            return;
        }
        final int padding = getResources().getDimensionPixelSize(R.dimen.margin);
        if (mapView.getMeasuredWidth() == 0) {
            ViewTreeObserver vto = mapView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver vto = mapView.getViewTreeObserver();
                    vto.removeOnGlobalLayoutListener(this);
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(b.build(), padding);
                    googleMap.moveCamera(cu);
                }
            });
        } else {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(b.build(), padding);
            googleMap.animateCamera(cu);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Map lifecycle callbacks
    ///////////////////////////////////////////////////////////////////////////

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
