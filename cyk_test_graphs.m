clc
clear

cases = ["TD_SG", "BU_SG", "TD_OC", "TD_COC", "TD_OOC", "TD_OO", "BU_OOC", "BU_OO", "BU_OC", "BU_COC"];
result = zeros(10, 21);
for k = 1 : 10

    s = cases(k);

    for i = 100:100:1000

        init_data = importdata(s + ".csv");
        data = init_data(:, [1,3:4]);
        
        temp = data(data(:, 1) == i, :);
        [~, r1] = min(temp(:, 2));
        [~, r2] = max(temp(:, 2));
        
        temp([r1, r2], :) = [];
    
        result(i/100, [k*2, (k*2)+1]) = [mean(temp(:, 2))*1.0e-09, i];

    end

    
end


% Stupid grammar
figure
plot(result(:, 3), result(:, 2))
grid('on')
hold on 
plot(result(:, 5), result(:, 4))
legend("bottom-up", "top-Down")
title('Parse times for the "stupid" grammar');
hold off

% Fast TD
figure
plot(result(:, 7), result(:, 6))
grid('on')
hold on 
plot(result(:, 9), result(:, 8))
legend("top-down ()..()", "top-Down )()..()")
title('Dyck language (top-down, fast cases)');
hold off

% Slow TD
figure
plot(result(:, 11), result(:, 10))
grid('on')
hold on 
plot(result(:, 13), result(:, 12))
legend("top-down ((..))", "top-Down ()..()(")
title('Dyck language (top-down, slow cases)');
hold off

% BU
figure
plot(result(:, 15), result(:, 14))
grid('on')
hold on 
plot(result(:, 17), result(:, 16))
hold on
plot(result(:, 19), result(:, 18))
hold on 
plot(result(:, 21), result(:, 20))
legend("bottom-up ((..))", "bottom-up ()..()", "bottom-up ()..()(", "bottom-up )()..()")
title('Dyck language (bottom-up)');
hold off



