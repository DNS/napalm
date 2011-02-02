package com.github.napalm.test.app;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@Data
public class NapalmTestUser {
	private int id;
	private String name;
}
