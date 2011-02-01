package com.github.napalm.test;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@Data
public class NapalmTestUser {
	private int id;
	private String name;
}
