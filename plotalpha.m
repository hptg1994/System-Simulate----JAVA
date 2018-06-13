function plotalpha()
    X = 0.0001:0.0001:.01;
    for i = 6:6
        file = strcat('data',  num2str(i), '.txt');
        fileID = fopen(file,'r');
        formatSpec = '%f';
        A = fscanf(fileID,formatSpec);
        fclose(fileID);
        hold on
        plot(X, A)
    end
end
    
    