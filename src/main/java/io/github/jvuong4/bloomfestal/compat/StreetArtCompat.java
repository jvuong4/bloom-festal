package io.github.jvuong4.bloomfestal.compat;

import com.streetart.StreetArt;
import io.github.jvuong4.bloomfestal.compat.StreetArt.*;

public class StreetArtCompat  implements BFCompat {

	@Override
	public void initialize() {
		StreetArtItems.init();
		StreetArtEntities.init();
	}
}
