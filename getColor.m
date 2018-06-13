function color = getColor(id)
    red = .3 + id/10;
    green = .6 + id/10;
    blue = .9 + id/10;
    
    red = mod(red * 10, 10)/10;
    green = mod(green * 10, 10)/10;
    blue = mod(blue * 10, 10)/10;
    
    color = [red green blue];
end
    