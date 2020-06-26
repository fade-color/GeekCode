package xyz.qinian.geekcode.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xyz.qinian.geekcode.Activity.ExamActivity;
import xyz.qinian.geekcode.R;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private View view;
    private JSONArray problems;

    public ExamAdapter(JSONArray problems) {
        this.problems = problems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView examTitle, examDetail;
        LinearLayout examItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            examTitle = itemView.findViewById(R.id.exam_title);
            examDetail = itemView.findViewById(R.id.exam_detail);
            examItem = itemView.findViewById(R.id.exam_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            JSONObject p = problems.getJSONObject(position);
            final int id = p.getInt("problemId");
            final String problemContent = p.getString("problemContent");
            final String submitNum = p.getString("submitNum");
            final String correctNum = p.getString("correctNum");
            holder.examTitle.setText(id + ". " + problemContent);
            holder.examDetail.setText("提交："+ submitNum +"   通过：" + correctNum);

            holder.examItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ExamActivity.class);
                    intent.putExtra("exam_id", id);
                    intent.putExtra("content", problemContent);
                    intent.putExtra("submitNum", submitNum);
                    intent.putExtra("correctNum", correctNum);
                    v.getContext().startActivity(intent);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }
}
