function plotTimeSlice()
    X = 1:1:800;
%     for i = 1:5
        file = strcat('rr_withContextSwitch.txt');
        fileID = fopen(file,'r');
        formatSpec = '%f';
        A = fscanf(fileID,formatSpec);
        fclose(fileID);
        figure
        plot(X, A)
%     end

        file = strcat('rr_noContextSwitch.txt');
        fileID = fopen(file,'r');
        formatSpec = '%f';
        A = fscanf(fileID,formatSpec);
        fclose(fileID);
        figure
        plot(X, A)
end