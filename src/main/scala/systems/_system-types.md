# Types of Systems
a list of potential systems

http://mtnphil.wordpress.com/2013/03/23/entity-component-system-framework-redux/
http://blog.lmorchard.com/2013/11/27/entity-component-system
http://gamadu.com/artemis/javadoc/index.html
http://t-machine.org/index.php/2010/05/09/entity-system-1-javaandroid/
http://gamedev.stackexchange.com/questions/61989/entity-component-systems-input-and-angry-birds
https://code.google.com/p/gamadu-tankz/source/browse/src/com/tankz/systems

* AmmoRegenerationSystem
* Bounce System
* BoundarySystem
* CameraSystem
* CatapultSystem
* Collision Detection System (MOVEABLE_ENTITIES)
* CrosshairRenderSystem
* DelayedEntityProcessingSystem
* EntanglementSystem: (This is another game-specific system. This looks at entities with the Entanglement Component, and ensures their positions and/or orientations are kept in sync with the entity’s entangled pair. It also draws the entanglement visuals (it is not only the RenderingSystem that is responsible for drawing))
* EntityProcessingSystem (A typical entity system. Use this when you need to process entities possessing the provided component types.)
* EntitySystem (The most raw entity system. It should not typically be used, but you can create your own entity system handling by extending this. It is recommended that you use the other provided entity system implementations.)
* ExpirationSystem
* FrameAnimationSystem (It tracks entities with both Aspect and FrameAnimation components, and adjusts the Aspect Components with the correct location in the spritesheet, based off the current time and the framerate specified in FrameAnimation Component.)
* HealthRenderSystem
* HealthSystem
* HealthSystem (has all entities with health component)
* HudRenderSystem
* InputSystem (knows about keyboard/mouse events such as clicked, dragged, released, key down etc, not sure what entities should have...)
* IntervalEntityProcessingSystem (If you need to process entities at a certain interval then use this. A typical usage would be to regenerate ammo or health at certain intervals, no need to do that every game loop, but perhaps every 100 ms. or every second.)
* IntervalEntitySystem (A system that processes entities at a interval in milliseconds. A typical usage would be a collision system or physics system.)
* LaserSystem ( This is the most complex system, and manages the main gameplay logic that involves entities with LaserInteractive Components.)
* MotionSystem: (Fetch all the Motion components from the database. For each Motion, look up a Position for the same Entity. Update the x, y, and rotation properties of the Position using the Motion properties.)
* MovementSystem
* Physics system (When it's integrating the acceleration and velocity, it can also add gravity. Something like this could work (don't actually use Euler integration, this is just for simplicity's sake))
* Physics system does most of what the Position System would do.
* PhysicsSystem
* PhysicsSystem (has all entities with position, velocity and physics components)
* PlayerTankMovementSystem
* PlayerTankTowerSystem
* Position System (https://github.com/kiith-sa/ICE/blob/master/component/physicssystem.d, https://code.google.com/p/gamadu-tankz/source/browse/src/com/tankz/systems/physics/PhysicsSystem)
* Position System (MOVEABLE_ENTITIES)
* Rendering System  (loops through all entities that have Aspect and Placement Components and prepares the necessary stuff to draw them. It doesn’t need to do anything special to determine which entities these are. It just states declaratively that it’s interested in entities with those two Components, and those entities’ ids are automatically added to a list in the System.)
* Rendering System (RENDERABLE_ENTITIES)
* RenderSystem
* SeekerSystem: (Fetch all the Seeker components. For each, find the Motion and Position components for the corresponding Entity. Also find Motion & Position for the Entity identified as the target_entity_id. Calculate the angle between seeker & target, update Motion to steer toward the target.)
* SoundSystem
* SpriteSystem (has all entities with sprite component)
* TerrainRenderSystem
* VoidEntitySystem (This system has an empty aspect so it processes no entities, but it still gets invoked. You can use this system if you need to execute some game logic and not have to concern yourself about aspects or entities.)
* Winning System: (This monitors certain entities’  state to see if the current level has been won. So you can see that most systems deal with one particular component, but also require a set of other components in order to do their job. Some systems operate over a similar set of entities (for instance, the LaserSystem and WinningSystem), but contain completely unrelated logic. So while they could be part of the same system, “one class, one purpose” dictates they should not be.)


"Collisions are handled by the physics system. And it can send messages to the health system to appropriately deduct health from baddies/birds and destroy the objects collided with."

