/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package oopsimpl;

import java.io.IOException;

public interface ChartsService {
    void piechart() throws Exception;
    void lineChart() throws IOException, InterruptedException;
    void multiLineChart() throws IOException, InterruptedException;
    void barChart() throws IOException, InterruptedException;
}
