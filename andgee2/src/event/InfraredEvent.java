///*
// * wiigee - accelerometerbased gesture recognition
// * Copyright (C) 2007, 2008 Benjamin Poppinga
// * 
// * Developed at University of Oldenburg
// * Contact: benjamin.poppinga@informatik.uni-oldenburg.de
// *
// * This file is part of wiigee.
// *
// * wiigee is free software; you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation; either version 2 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License along
// * with this program; if not, write to the Free Software Foundation, Inc.,
// * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
// */
//
//package event;
//
//import java.util.EventObject;
//import device.Wiimote;
//
//public class InfraredEvent extends EventObject {
//
//	protected Wiimote wiimote;
//	protected int[][] coordinates;
//	protected int[] size;
//	protected boolean[] valid;
//	
//	public InfraredEvent(Wiimote source, int[][] coordinates, int[] size) {
//		super(source);
//		this.coordinates=coordinates;
//		this.size=size;
//		this.valid = new boolean[4];
//		for(int i=0; i<this.coordinates.length; i++) {
//			this.valid[i] = (this.coordinates[i][0]<1023 && this.coordinates[i][1]<1023);
//		}
//	}
//	
//	public Wiimote getSource() {
//		return this.wiimote;
//	}
//	
//	public boolean isValid(int i) {
//		return this.valid[i];
//	}
//	
//	public int[][] getCoordinates() {
//		return this.coordinates;
//	}
//	
//	public int[] getSize() {
//		return this.size;
//	}
//
//}
