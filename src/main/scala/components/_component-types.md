# Component Types
a list of possible component types found so far

http://blog.lmorchard.com/2013/11/27/entity-component-system#Components
https://gamedev.stackexchange.com/questions/56519/movement-physics-in-an-entity-component-system
http://entity-systems.wikidot.com/using-an-es-with-physics

* Aspect: defines what the entity looks like (texture info, color, etc…)
* BeamWeapon – max_power, current_power, recharge_per_second
* Camera
* Collision component
* Collision component (boundaries)
* Crawl
* Damage (hitpoints)
* Display
* Drawing
* DummyPositionComponent
* Entanglement: another game-specific component. It is used to link one entity’s position or orientation to another
* FrameAnimation: indicates that the entity should use spritesheet animation, and specifies things like framerate.
* GraphicsComponent
* GraphicsComponent
* Gravity
* GUN_COMPONENT
* Health
* Health – max_health, current_health
* Input component (would have fields that describe how the entity wants to move, such as: walk left, jump, and attack. This can be supplied by the keyboard, by an AI, or over the network, which is implemented as a control component.)
* Input: defines whether or not this entity fires click/drag events when interacted with by the mouse or touch
* InputController
* Jump
* Jump
* Jumping
* LaserInteractive: this is something specific to this particular game, and contains information about a core gameplay feature
* Light
* Menu
* MeshShape
* MissileWeapon – number_turrets, reload_delay
* Motion – dx, dy, drotation
* PhysicalBody
* Physics
* Physics (velocity, acceleration, mass)
* PHYSICS_COMPONENT
* PhysicsCharacter
* PhysicsComponent
* Placement
* Placement: defines the entity’s location and orientation
* Player component(tagging class)
* PlayerControls
* Position
* Position
* Position – x, y, rotation
* PositionComponent
* Rectangle
* Renderable
* Renderable
* Renderable component
* RENDERABLE_COMPONENT
* RenderableMesh
* Renderer
* Scripts: this is a bit of a special one, and describes which custom scripts are associated with the entity.
* Seeker – target_entity_id, rotation_per_second
* SoundLoop: indicates a sound that is to be played (and volume, etc..).
* Sprite – width, height, shape
* Thruster – accel_per_second, max_speed
* TRACKED_COMPONENT
* Transform
* Transform(Position) component
* Velocity
* Velocity
* Walk
* Walking
* Weapon
* Velocity component
* catapult control component

Bullet
  damage
  range

Display
  object
  layer

Enemy
  prey

Gun
  timeSinceLastShot
  bulletLifetime

Hero (marker class)

HeroControl
  leftKey, rightKey, upKey, downKey
  attackKey, runKey
  rotationSpeed

Life
  health, maxHealth
  stamina, maxStamina

Motion
  velocityX, velocityY
  friction

Position
  x, y, rotation

Predator
  prey

Prey
  predator

Stalker
  prey