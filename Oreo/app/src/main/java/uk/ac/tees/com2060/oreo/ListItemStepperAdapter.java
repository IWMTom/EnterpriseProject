package uk.ac.tees.com2060.oreo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListItemStepperAdapter extends AbstractFragmentStepAdapter
{

    private ArrayList<Step> fragments = new ArrayList<>();

    public ListItemStepperAdapter(FragmentManager fm, Context context)
    {
        super(fm, context);

        Step step1 = new ListItemStep1Fragment();
        Step step2 = new ListItemStep2Fragment();
        Step step3 = new ListItemStep3Fragment();

        fragments.add(step1);
        fragments.add(step2);
        fragments.add(step3);
    }

    public ArrayList<Step> getFragments()
    {
        return fragments;
    }


    @Override
    public Step createStep(int position)
    {
        switch (position)
        {
            case 0:
                return fragments.get(0);
            case 1:
                return fragments.get(1);
            case 2:
                return fragments.get(2);
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position)
    {
        StepViewModel.Builder builder = new StepViewModel.Builder(context);

        switch (position)
        {
            case 0:
                builder
                        .setTitle("Select Location");
                break;
            case 1:
                builder
                        .setTitle("Item Details");
                break;
            case 2:
                builder
                        .setTitle("Confirm");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }

        return builder.create();
    }
}