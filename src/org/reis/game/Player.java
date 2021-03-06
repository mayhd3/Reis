package org.reis.game;

import org.joml.Quaterniond;
import org.joml.Vector2d;
import org.joml.Vector3d;

public class Player extends Entity {
    private double speed = 32.0;

    public Player(World world) {
        super(world);
    }

    public void walk( Vector3d move ) {
        // Sense on XZ plane
        double pitch = Math.asin( getOrientation()
            .transformUnitPositiveZ( new Vector3d() )
            .normalize()
            .y()
        );

        // Walk on global XZ plane
        if( move.lengthSquared() != 0 ) act( new Vector3d(move)
            //rotateXYZ(getOrientation())
            .rotate(new Quaterniond(getOrientation()).rotateX(pitch))
            .setComponent(1,move.y())
            .normalize(speed)
        );
        else if( getVelocity().lengthSquared() != 0 && getVelocity().y() == 0 ) act( new Vector3d( getVelocity() )
            .mul( speed )
            .negate()
        );
    }
    public void look( Vector2d turn ) {
        //getOrientation().rotateXYZ(look.y()/1024, look.x()/1024, 0);
        //balance();

        // Global yaw, local pitch
        getOrientation().rotateX( turn.y()/1024 ).rotateLocalY( turn.x()/1024 );

        // Constrain yaw to +-180deg
        double flip = getOrientation().normalizedPositiveY( new Vector3d( ) ).y();
        if ( flip < 0 ) getOrientation().rotateX( Math.copySign(
            Math.asin( flip ),
            getOrientation().transformUnitPositiveZ( new Vector3d( ) ).y()
        ) );
    }
    private void balance( ) {
        // Roll towards local Y -> global Y
        getOrientation().rotateZ( -Math.asin( getOrientation().transformPositiveX( new Vector3d( ) ).y() ) );
    }
}
