import processing.core.PImage;
import java.util.List;

abstract class Entity
{

   private Point position;
   private List<PImage> images;
   private int imageIndex;

   public Entity(Point position, List<PImage> images)
   {
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
   }

   protected Point getPosition() {
      return position;
   }

   public List<PImage> getImages() {
      return images;
   }

   public int getImageIndex() {
      return imageIndex;
   }

   void setPosition(Point p){
      position = p;
   }

   public PImage getCurrentImage()
   {
      return images.get(imageIndex);
   }

   public void setImageIndex(int i) {
      imageIndex = i;
   }

}
