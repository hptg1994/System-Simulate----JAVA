function plotFCFSGantt()
    file = strcat('fcfsGantt');
    fileID = fopen(file,'r');
    formatSpec = '%d %d %d';
    Asize = [3 Inf];
    A = fscanf(fileID,formatSpec, Asize);
    fclose(fileID)
    [row, colum] = size(A);
    
    for i = 1:colum
        colum_unit = A(:,i);
        color = getColor(colum_unit(1));
%         disp(color)
        hold on
        rectangle('Position',[colum_unit(2) colum_unit(1)-1 colum_unit(3) 1],'FaceColor',color);
    end
end