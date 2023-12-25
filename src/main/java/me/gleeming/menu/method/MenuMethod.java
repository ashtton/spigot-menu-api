package me.gleeming.menu.method;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.gleeming.menu.Menu;

import java.lang.reflect.Method;

@RequiredArgsConstructor
@Getter
public class MenuMethod {

    private final Menu menu;

    private final Method method;

    private final Object parent;

}
