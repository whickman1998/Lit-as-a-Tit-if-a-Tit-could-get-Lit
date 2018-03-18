import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class Soldier extends Mover {
	
	private boolean weapons; 
	private static final String QUAKE_KEY = "quake";

	public Soldier(Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
		super(position, images, actionPeriod, animationPeriod);
		weapons = false; 
	}
	
	public void executeActivity(WorldModel world,
            ImageStore imageStore, EventScheduler scheduler) {
		if (!weapons) {
			Optional<Entity> unarmedTarget = world.findNearest(super.getPosition(), Weapon.class);

			if (unarmedTarget.isPresent() &&
					moveTo(world, unarmedTarget.get(), scheduler))
			{
				weapons = true; 
			}
			else
			{
				scheduler.scheduleEvent(this,
						Factory.createActivityAction(this, world, imageStore),
						super.getActionPeriod());
			}
		}
		else {
			Optional<Entity> armedTarget = world.findNearest(
	                super.getPosition(), Ore_Blob.class);
	        long nextPeriod = super.getActionPeriod();

	        if (armedTarget.isPresent())
	        {
	            Point tgtPos = armedTarget.get().getPosition();

	            if (moveTo(world, armedTarget.get(), scheduler))
	            {
	                Quake quake = Factory.createQuake(tgtPos, imageStore.getImageList(QUAKE_KEY));

	                world.addEntity(quake);
	                nextPeriod += super.getActionPeriod();
	                quake.scheduleActions(scheduler, world, imageStore);
	            }
	        }

	        scheduler.scheduleEvent(this,
	                Factory.createActivityAction(this, world, imageStore),
	                nextPeriod);
		}
		 
	}

	public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) { 
		
		if (getPosition().adjacent(getPosition(), target.getPosition())) {
			world.removeEntity(target);
			scheduler.unscheduleAllEvents(target);
			return true;
		}
		else { 
			Point nextPos = nextPosition(world, target.getPosition());

			if (!getPosition().equals(nextPos)) {
				Optional<Entity> occupant = world.getOccupant(nextPos);
				if (occupant.isPresent()) {
					scheduler.unscheduleAllEvents(occupant.get());
				}
				
				world.moveEntity(this, nextPos);
			}
			return false;
		}
	}

}
