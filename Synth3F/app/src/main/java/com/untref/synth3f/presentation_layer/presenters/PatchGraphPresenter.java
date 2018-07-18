package com.untref.synth3f.presentation_layer.presenters;

import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.untref.synth3f.domain_layer.helpers.PatchGraphManager;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.domain_layer.helpers.BaseProcessor;
import com.untref.synth3f.entities.DACPatch;
import com.untref.synth3f.entities.EGPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCAPatch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.View.MapView;
import com.untref.synth3f.presentation_layer.View.PatchDACView;
import com.untref.synth3f.presentation_layer.View.PatchEGView;
import com.untref.synth3f.presentation_layer.View.PatchVCAView;
import com.untref.synth3f.presentation_layer.View.PatchVCFView;
import com.untref.synth3f.presentation_layer.View.PatchVCOView;
import com.untref.synth3f.presentation_layer.View.PatchView;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

import java.util.ArrayList;
import java.util.List;


public class PatchGraphPresenter {

    private PatchGraphFragment patchGraphFragment;
    //    private SparseArray<PatchView> patches;
//    private SparseArray<List<Connection>> connectionsSet;
    private BaseProcessor processor;
    private PatchGraphManager patchGraphManager;

    private int dragPatchOrigin;
    private View dragOutlet;

    public PatchGraphPresenter(PatchGraphFragment patchGraphFragment, BaseProcessor processor) {
        this.patchGraphFragment = patchGraphFragment;
        this.processor = processor;
        this.patchGraphManager = new PatchGraphManager();
//        this.patches = new SparseArray<>();
//        this.connectionsSet = new SparseArray<>();
    }

    public BaseProcessor getProcessor() {
        return processor;
    }

    public PatchGraphFragment getPatchGraphFragment() {
        return patchGraphFragment;
    }

    public PatchView createPatch(String type) {
        Patch patch;
        PatchView patchView;
        switch (type) {
            case "x_vco":
                patch = new VCOPatch();
                patchView = new PatchVCOView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
            case "x_vca":
                patch = new VCAPatch();
                patchView = new PatchVCAView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
            case "x_vcf":
                patch = new VCFPatch();
                patchView = new PatchVCFView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
            case "x_eg":
                patch = new EGPatch();
                patchView = new PatchEGView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
            case "x_dac":
                patch = new DACPatch();
                patchView = new PatchDACView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
//            case "x_kb_":
//                patchView = new MapItemViewKB(MainActivity.this, wireDrawer, maxPosition, newId);
//                break;
            default:
                patch = new VCOPatch();
                patchView = new PatchVCOView(patchGraphFragment.getActivity(), patchGraphFragment.getWireDrawer(), patchGraphFragment.getPatchGraphPresenter(), patch);
                break;
        }
        patchGraphManager.addPatch(patch);
        int newId = patchGraphFragment.findUnusedId();
        patchView.setId(newId);
        patchView.setPatchId(patch.getId());
        processor.createPatch(type, patch.getId());
        patch.initialize(processor);
        return patchView;
    }

    public void setDragOn(int patchId, View outlet) {
        dragPatchOrigin = patchId;
        dragOutlet = outlet;
    }

    public void tryConnect(int x, int y) {
        Rect bounds;
        int[] location;
        ConstraintLayout map = (ConstraintLayout) patchGraphFragment.getMapView().getChildAt(0);
        float scale = patchGraphFragment.getMapView().getScale();
        for (int i = 0; i < map.getChildCount() - 1; i++) {
            bounds = new Rect();
            PatchView patch = (PatchView) map.getChildAt(i);
            if (patch.getPatchId() != dragPatchOrigin) {
                for (AppCompatImageView inlet : patch.getInputs()) {
                    inlet.getHitRect(bounds);
                    location = getPositionOfView(inlet);
                    bounds.left *= scale;
                    bounds.right *= scale;
                    bounds.top *= scale;
                    bounds.bottom *= scale;
                    if (bounds.contains(x - location[0] + bounds.left, y - location[1] + bounds.top)) {
                        Connection connection = patchGraphManager.connect(dragPatchOrigin, (int) dragOutlet.getTag(), patch.getPatchId(), (int) inlet.getTag());
                        patchGraphFragment.getWireDrawer().addConnection(connection, dragOutlet, inlet);
                        processor.connect(connection);
                    }
                }
            }
        }
    }

    public Patch getPatch(int patchId) {
        return patchGraphManager.getPatch(patchId);
    }

    public Patch delete(int patchId) {
        Patch patch = patchGraphManager.removePatch(patchId);
        patchGraphFragment.getWireDrawer().removePatch(patch);
        return patch;
    }

    private int[] getPositionOfView(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position;
    }
}