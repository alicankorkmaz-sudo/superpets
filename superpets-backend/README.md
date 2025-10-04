# Ktor Superpets - Nano Banana API

This project was created using the [Ktor Project Generator](https://start.ktor.io) and includes an endpoint for Google's Nano Banana image editing model via fal.ai.

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                               | Description                                                 |
| ----------------------------------------------------|------------------------------------------------------------- |
| [Routing](https://start.ktor.io/p/routing-default) | Allows to define structured routes and associated handlers. |
| Nano Banana API Integration                        | Image editing endpoint using Google's Nano Banana model via fal.ai |

## Nano Banana Edit Endpoint

**POST** `/nano-banana/edit`

### Headers
- `X-API-Key`: Your fal.ai API key (required)
- `Content-Type`: application/json

### Request Body
```json
{
  "prompt": "make a photo of the man driving the car down the california coastline",
  "image_urls": [
    "https://storage.googleapis.com/falserverless/example_inputs/nano-banana-edit-input.png",
    "https://storage.googleapis.com/falserverless/example_inputs/nano-banana-edit-input-2.png"
  ],
  "num_images": 1,
  "output_format": "jpeg",
  "sync_mode": false
}
```

### Response
```json
{
  "images": [
    {
      "url": "https://storage.googleapis.com/falserverless/example_outputs/nano-banana-multi-edit-output.png"
    }
  ],
  "description": "Here is a photo of the man driving the car down the California coastline."
}
```

### Example cURL Request
```bash
curl -X POST http://localhost:8080/nano-banana/edit \
  -H "Content-Type: application/json" \
  -H "X-API-Key: YOUR_FAL_AI_API_KEY" \
  -d '{
    "prompt": "make a photo of the man driving the car down the california coastline",
    "image_urls": [
      "https://storage.googleapis.com/falserverless/example_inputs/nano-banana-edit-input.png"
    ],
    "num_images": 1,
    "output_format": "jpeg"
  }'
```

### Parameters

- **prompt** (required): Description of the desired image edit
- **image_urls** (required): Array of image URLs to edit (1-4 images)
- **num_images** (optional): Number of output images (1-4, default: 1)
- **output_format** (optional): "jpeg" or "png" (default: "jpeg")
- **sync_mode** (optional): Return images as data URIs instead of URLs (default: false)

### Implementation Notes

This implementation uses Ktor's HTTP client to make direct API calls to fal.ai. While fal.ai provides an official [Kotlin client library](https://docs.fal.ai/model-apis/clients/kotlin/), we opted for the HTTP client approach for simplicity and direct control over the request/response handling.

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

