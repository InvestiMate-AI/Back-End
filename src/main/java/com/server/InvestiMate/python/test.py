import json
import sys

def get_feedback(name, date, trade_type):
    # 반환할 JSON 데이터 작성
    feedback_data = [
        {
            "index": 0,
            "type": name,
            "data": json.dumps({"Open": f"Processed feedback for {name} on {date} with trade type {trade_type}"})
        },
        {
            "index": 1,
            "type": date,
            "data": json.dumps({"Open": f"Processed feedback for {name} on {date} with trade type {trade_type}"})
        },
        {
            "index": 2,
            "type": trade_type,
            "data": json.dumps({"Open": f"Processed feedback for {name} on {date} with trade type {trade_type}"})
        }
    ]
    print(json.dumps(feedback_data))
    return feedback_data

if __name__ == '__main__':
    func_name = sys.argv[1]
    var = sys.argv[2:]
    if func_name == 'get_feedback':
        get_feedback(var[0], var[1], var[2])