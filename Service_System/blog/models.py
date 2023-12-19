from django.conf import settings
from django.db import models
from django.utils import timezone


class Post(models.Model):
    models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    title = models.CharField(max_length=200)
    text = models.TextField()
    created_date = models.DateTimeField(
            default=timezone.now)
    published_date = models.DateTimeField(
            blank=True, null=True)
    image = models.ImageField(upload_to='intruder_image/%Y/%m/%d')

    def publish(self):
        self.published_date = timezone.now()
        self.save()

    def __str__(self):
        return self.title


class Client(models.Model):
    name = models.CharField(max_length=50, unique=True, null=False)
    state = models.CharField(max_length=50, default="sleep", null=False)
    host = models.CharField(max_length=50, default="0.0.0.0", null=False)

    def __str__(self):
        return self.name
