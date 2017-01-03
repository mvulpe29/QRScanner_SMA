package mvulpe.scanner.Reader;

/**
 * Created by Mihai on 07/12/2016.
 */

import mvulpe.scanner.Reader.ui.GraphicOverlay;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private BarcodeGraphicTracker.BarcodeAutoDetectionListener barcodeAutoDetectionListener;

    BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay) {
        mGraphicOverlay = barcodeGraphicOverlay;
    }

    BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeGraphicTracker.BarcodeAutoDetectionListener barcodeAutoDetectionListener) {
        mGraphicOverlay = barcodeGraphicOverlay;
        this.barcodeAutoDetectionListener = barcodeAutoDetectionListener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        BarcodeGraphicTracker tracker = new BarcodeGraphicTracker(mGraphicOverlay, graphic);
        if(barcodeAutoDetectionListener!=null){
            tracker.setNewDetectionListener(barcodeAutoDetectionListener);
        }
        return tracker;
    }

}

