package com.javagrunt.service.opencvcapture.capture;

import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.opencv.opencv_core.Mat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
class CaptureController {

    private BufferedImage captureFrame() throws IOException {
        try (FrameGrabber grabber = new OpenCVFrameGrabber(0)) {
            grabber.start();
            Mat grabbedImage;
            try (OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat()) {
                grabbedImage = converter.convert(grabber.grab());
            }
            grabber.stop();
            return Java2DFrameUtils.toBufferedImage(grabbedImage);
        }
    }

    // Endpoint to return captured image as JPEG byte array
    @ResponseBody
    @GetMapping(value = "/capture", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getCapturedImage() {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            BufferedImage image = captureFrame();
            javax.imageio.ImageIO.write(image, "jpg", os);
            return os.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }

    @ResponseBody
    @GetMapping(value = "/encoded", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getEncodedImage() {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            BufferedImage image = captureFrame();
            javax.imageio.ImageIO.write(image, "jpg", os);
            byte[] imageBytes = os.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            return "Error capturing image";
        }
    }
}
