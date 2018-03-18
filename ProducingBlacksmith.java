import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class ProducingBlacksmith extends JustActivityActions {

	public ProducingBlacksmith(Point position, List<PImage> images, int actionPeriod) {
		super(position, images, actionPeriod);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		// TODO Auto-generated method stub
		Optional<Point> openPt = world.findOpenAround(super.getPosition());

        if (openPt.isPresent())
        {
            Weapon weapon = Factory.createWeapon(openPt.get(), imageStore.getImageList(Factory.getWeaponKey()));
            world.addEntity(weapon); 
        }

        scheduler.scheduleEvent(this,
                Factory.createActivityAction(this, world, imageStore),
                super.getActionPeriod());
	}

}
