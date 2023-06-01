gcloud builds submit --tag gcr.io/<project-id>/index
gcloud run deploy --image gcr.io/<project-id>/index --platform managed