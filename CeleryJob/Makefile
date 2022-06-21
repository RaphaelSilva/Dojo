tasks =	celery -A tasks

worker:
	$(tasks) \
		worker --loglevel=INFO 

flower:
	$(tasks) flower --address=localhost --port=8088
