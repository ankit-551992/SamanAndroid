package com.qtech.saman.ui.test;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import com.qtech.saman.R;
import com.qtech.saman.base.BaseActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class TileOverlayDemoActivity extends BaseActivity
        implements SeekBar.OnSeekBarChangeListener, OnMapReadyCallback {

    private static final int TRANSPARENCY_MAX = 100;

    /** This returns moon tiles. */
//    private static final String MOON_MAP_URL_FORMAT =
//            "http://mw1.google.com/mw-planetary/lunar/lunarmaps_v1/clem_bw/%d/%d/%d.jpg";
  private static final String MOON_MAP_URL_FORMAT =
            "http://www.gravatar.com/avatar/a14f2a08ae523d5a674b719eae03c428?s=256&d=identicon&r=PG";
private static final String MOON_MAP_URL_FORMAT2 =
            "http://www.gravatar.com/avatar/3c9ccfaa084eb12cc0b72e13962003e7?s=256&d=identicon&r=PG";

    private TileOverlay mMoonTiles;
    private SeekBar mTransparencyBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tile_overlay_demo);

        mTransparencyBar = (SeekBar) findViewById(R.id.transparencySeekBar);
        mTransparencyBar.setMax(TRANSPARENCY_MAX);
        mTransparencyBar.setProgress(0);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        TileProvider tileProvider = new UrlTileProvider(256, 256) {
            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                // The moon tile coordinate system is reversed.  This is not normal.
                int reversedY = (1 << zoom) - y - 1;
                String s = String.format(Locale.US, MOON_MAP_URL_FORMAT, zoom, x, reversedY);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };

        mMoonTiles = map.addTileOverlay(new TileOverlayOptions().tileProvider(tileProvider));
        mTransparencyBar.setOnSeekBarChangeListener(this);
    }

    public void setFadeIn(View v) {
        if (mMoonTiles == null) {
            return;
        }
        mMoonTiles.setFadeIn(((CheckBox) v).isChecked());
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMoonTiles != null) {
            mMoonTiles.setTransparency((float) progress / (float) TRANSPARENCY_MAX);
        }
    }
}
