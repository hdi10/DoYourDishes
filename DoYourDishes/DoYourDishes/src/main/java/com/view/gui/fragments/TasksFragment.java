package com.view.gui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.control.controllerLogic.PlanLogic.PlanController;
import com.control.controllerLogic.PlanLogic.fragmentControllers.Tasks.RecyclerViewAdapterTask;
import com.control.controllerLogic.PlanLogic.fragmentControllers.Tasks.TaskFragmentController;
import com.model.dataModel.Task;
import com.view.R;
import com.view.gui.PlanActivity;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment implements TasksFragmentInterface {


    private static final String TAG = "TasksFragment";
    private PlanActivity planActivity;
    private PlanController planController;
    private TaskFragmentController taskFragmentController;
    private RecyclerView recyclerView;
    private View rootView;

    public TasksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        planActivity = (PlanActivity) getActivity();
        Log.d(TAG, "onCreateView: " + planActivity);
        this.planController = planActivity.getPlanController();
        planController.fetchData();
        Log.d(TAG, "onCreateView: " + planController);

        this.taskFragmentController = new TaskFragmentController(this.planController, this);

        View RootView = inflater.inflate(R.layout.fragment_tasks, container, false);
        List<Task> taskList = new ArrayList<Task>();
        this.recyclerView = (RecyclerView) RootView.findViewById(R.id.tasksFragmentRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        this.recyclerView.setLayoutManager(llm);
        RecyclerViewAdapterTask taskAdapter = new RecyclerViewAdapterTask(taskList, this.planController);
        this.recyclerView.setAdapter(taskAdapter);

        this.rootView = RootView;
        return RootView;
    }

    @Override
    public void renderData(List<Task> tasksToRender) {
        RecyclerViewAdapterTask newTaskAdapter = new RecyclerViewAdapterTask(tasksToRender, this.planController);
        this.recyclerView.setAdapter(newTaskAdapter);
    }

    @Override
    public void addTask() {
        taskFragmentController.addTask();
    }
}